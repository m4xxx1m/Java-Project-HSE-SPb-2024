package com.example.server.service;

import com.example.server.dto.ResumeDto;
import com.example.server.model.FileInfo;
import com.example.server.model.User;
import com.google.gson.Gson;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private static final String RESUME_DIRECTORY_PATH = "src\\main\\resources\\resume\\";

    private final FileInfoService fileInfoService;

    private final UserService userService;

    public void createResume(ResumeDto resumeDtoStruct, int userId) throws IOException, InterruptedException {
//        ResumeDto resumeDtoStruct = new Gson().fromJson(resumeDto, ResumeDto.class);
        String tex = generateText(resumeDtoStruct);
        File tempFile = File.createTempFile("temp", ".tex");
        Writer writer = new OutputStreamWriter(new FileOutputStream(tempFile), StandardCharsets.UTF_8);
        writer.write(tex);
        writer.close();

        // Install pdflatex and add to PATH, may need to restart the machine.
        ProcessBuilder pb = new ProcessBuilder("pdflatex", "-output-directory", tempFile.getParent(), tempFile.getAbsolutePath());
        pb.redirectError(ProcessBuilder.Redirect.INHERIT);
        pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        Process process = pb.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("pdflatex failed with exit code " + exitCode);
        }

        File pdfFile = new File(tempFile.getParent(), tempFile.getName().replace(".tex", ".pdf"));

        MultipartFile multipartFile = new MockMultipartFile(pdfFile.getName(), new FileInputStream(pdfFile));

        userService.uploadResume(userId, multipartFile);

        FileInfo fileInfo = fileInfoService.findById(userService.getUser(userId).getResumeInfoId());
        fileInfo.setFileType("application/pdf");
        fileInfoService.saveFileInfo(fileInfo);

        tempFile.delete();
        pdfFile.delete();
    }

    private String generateText(ResumeDto resumeDto) {
        StringBuilder tex = new StringBuilder("%-------------------------\n" +
                "% Resume in Latex\n" +
                "% Author : Jake Gutierrez\n" +
                "% Based off of: https://github.com/sb2nov/resume\n" +
                "% License : MIT\n" +
                "%------------------------\n\n" +
                "\\documentclass[letterpaper,11pt]{article}\n\n" +
                "\\usepackage{latexsym}\n" +
                "\\usepackage[empty]{fullpage}\n" +
                "\\usepackage{titlesec}\n" +
                "\\usepackage{marvosym}\n" +
                "\\usepackage[usenames,dvipsnames]{color}\n" +
                "\\usepackage{verbatim}\n" +
                "\\usepackage{enumitem}\n" +
                "\\usepackage[hidelinks]{hyperref}\n" +
                "\\usepackage{fancyhdr}\n" +
                "\\usepackage[english, russian]{babel}\n" +
                "\\usepackage{tabularx}\n" +
                "\\input{glyphtounicode}\n\n\n" +
                "%----------FONT OPTIONS----------\n" +
                "% sans-serif\n" +
                "% \\usepackage[sfdefault]{FiraSans}\n" +
                "% \\usepackage[sfdefault]{roboto}\n" +
                "% \\usepackage[sfdefault]{noto-sans}\n" +
                "% \\usepackage[default]{sourcesanspro}\n\n" +
                "% serif\n" +
                "% \\usepackage{CormorantGaramond}\n" +
                "% \\usepackage{charter}\n\n\n" +
                "\\pagestyle{fancy}\n" +
                "\\fancyhf{} % clear all header and footer fields\n" +
                "\\fancyfoot{}\n" +
                "\\renewcommand{\\headrulewidth}{0pt}\n" +
                "\\renewcommand{\\footrulewidth}{0pt}\n\n" +
                "% Adjust margins\n" +
                "\\addtolength{\\oddsidemargin}{-0.5in}\n" +
                "\\addtolength{\\evensidemargin}{-0.5in}\n" +
                "\\addtolength{\\textwidth}{1in}\n" +
                "\\addtolength{\\topmargin}{-.5in}\n" +
                "\\addtolength{\\textheight}{1.0in}\n\n" +
                "\\urlstyle{same}\n\n" +
                "\\raggedbottom\n" +
                "\\raggedright\n" +
                "\\setlength{\\tabcolsep}{0in}\n\n" +
                "% Sections formatting\n" +
                "\\titleformat{\\section}{\n" +
                "  \\vspace{-4pt}\\scshape\\raggedright\\large\n" +
                "}{}{0em}{}[\\color{black}\\titlerule \\vspace{-5pt}]\n\n" +
                "% Ensure that generate pdf is machine readable/ATS parsable\n" +
                "\\pdfgentounicode=1\n\n" +
                "%-------------------------\n" +
                "% Custom commands\n" +
                "\\newcommand{\\resumeItem}[1]{\n" +
                "  \\item\\small{\n" +
                "    {#1 \\vspace{-2pt}}\n" +
                "  }\n}\n\n" +
                "\\newcommand{\\resumeSubheading}[4]{\n" +
                "  \\vspace{-2pt}\\item\n" +
                "    \\begin{tabular*}{0.97\\textwidth}[t]{l@{\\extracolsep{\\fill}}r}\n" +
                "      \\textbf{#1} & #2 \\\\\n" +
                "      \\textit{\\small#3} & \\textit{\\small #4} \\\\\n" +
                "    \\end{tabular*}\\vspace{-7pt}\n}\n\n" +
                "\\newcommand{\\resumeSubSubheading}[2]{\n" +
                "    \\item\n" +
                "    \\begin{tabular*}{0.97\\textwidth}{l@{\\extracolsep{\\fill}}r}\n" +
                "      \\textit{\\small#1} & \\textit{\\small #2} \\\\\n" +
                "    \\end{tabular*}\\vspace{-7pt}\n}\n\n" +
                "\\newcommand{\\resumeProjectHeading}[2]{\n" +
                "    \\item\n" +
                "    \\begin{tabular*}{0.97\\textwidth}{l@{\\extracolsep{\\fill}}r}\n" +
                "      \\small#1 & #2 \\\\\n" +
                "    \\end{tabular*}\\vspace{-7pt}\n}\n\n" +
                "\\newcommand{\\resumeSubItem}[1]{\\resumeItem{#1}\\vspace{-4pt}}\n\n" +
                "\\renewcommand\\labelitemii{$\\vcenter{\\hbox{\\tiny$\\bullet$}}$}\n\n" +
                "\\newcommand{\\resumeSubHeadingListStart}{\\begin{itemize}[leftmargin=0.15in, label={}]}\n" +
                "\\newcommand{\\resumeSubHeadingListEnd}{\\end{itemize}}\n" +
                "\\newcommand{\\resumeItemListStart}{\\begin{itemize}}\n" +
                "\\newcommand{\\resumeItemListEnd}{\\end{itemize}\\vspace{-5pt}}\n\n" +
                "%-------------------------------------------\n" +
                "%%%%%%  RESUME STARTS HERE  %%%%%%%%%%%%%%%%%%%%%%%%%%%%\n\n\n" +
                "\\begin{document}\n\n" +
                "%----------HEADING----------\n\n" +
                "\\begin{center}\n" +
                "    \\textbf{\\Huge \\scshape " + resumeDto.getName() + " " + resumeDto.getSurname() + "} \\\\ \\vspace{1pt}\n");
        for (int i = 0; i < resumeDto.getContacts().size(); i++) {
            String contact = resumeDto.getContacts().get(i);
            tex.append(i == 0 ? "" : "$|$ \n").append(" \\href{").append(contact).append("}{\\underline{").append(contact).append("}}\n");
        }
        tex.append("\\end{center}\n\n\n");
        if (!resumeDto.getEducations().isEmpty()) {
            tex.append("%-----------EDUCATION-----------\n" + "\\section{Education}\n" + "  \\resumeSubHeadingListStart\n");
            for (int i = 0; i < resumeDto.getEducations().size(); i++) {
                ResumeDto.Education education = resumeDto.getEducations().get(i);
                tex.append("    \\resumeSubheading\n" + "{").append(education.getOrganization()).append("}{").append(education.getLocation()).append("}\n").append("{").append(education.getMajor()).append(", ").append(education.getDegree()).append("}{").append(education.getStudyPeriod()).append("}\n");
            }
            tex.append("\\resumeSubHeadingListEnd\n\n\n");
        }
        if (!resumeDto.getExperiences().isEmpty()) {
            tex.append("%-----------EXPERIENCE-----------\n" + "\\section{Experience}\n" + "  \\resumeSubHeadingListStart\n" + "\n");
            for (int i = 0; i < resumeDto.getExperiences().size(); i++) {
                ResumeDto.Experience experience = resumeDto.getExperiences().get(i);
                tex.append("    \\resumeSubheading\n" + "{").append(experience.getPosition()).append("}{").append(experience.getWorkPeriod()).append("}\n").append("{").append(experience.getOrganization()).append("}{").append(experience.getLocation()).append("}\n").append("\\resumeItemListStart\n");
                for (String task : experience.getTasks()) {
                    tex.append("\\resumeItem{").append(task).append("}\n");
                }
                tex.append("\\resumeItemListEnd\n\n");
            }
            tex.append("  \\resumeSubHeadingListEnd\n" + "\n" + "\n");
        }
        if (!resumeDto.getProjects().isEmpty()) {
            tex.append("%-----------PROJECTS-----------\n" + "\\section{Projects}\n" + "    \\resumeSubHeadingListStart\n");
            for (ResumeDto.Project project : resumeDto.getProjects()) {
                tex.append("\\resumeProjectHeading\n" + "{\\textbf{").append(project.getProjectName()).append("} $|$ \\emph{").append(project.getUsedTechnologies()).append("}}{").append(project.getWorkPeriod()).append("}\n").append("\\resumeItemListStart\n");
                for (String task : project.getTasks()) {
                    tex.append("\\resumeItem{").append(task).append("}\n");
                }
                tex.append("\\resumeItemListEnd\n");
            }
            tex.append("\\resumeSubHeadingListEnd\n\n\n");
        }
        tex.append("%-----------PROGRAMMING SKILLS-----------\n" + "\\section{Programming skills}\n" + " \\begin{itemize}[leftmargin=0.15in, label={}]\n" + "    \\small{\\item{\n" + "     \\textbf{Programming languages}{: ");
        for (int i = 0; i < resumeDto.getProgrammingLanguages().size(); i++) {
            tex.append(i == 0 ? "" : ", ").append(resumeDto.getProgrammingLanguages().get(i));
        }
        tex.append("}\\\\\n" + "     \\textbf{Frameworks}{: ");
        for (int i = 0; i < resumeDto.getFrameworks().size(); i++) {
            tex.append(i == 0 ? "" : ", ").append(resumeDto.getFrameworks().get(i));
        }
        tex.append("}\\\\\n" + "     \\textbf{Technologies}{: ");
        for (int i = 0; i < resumeDto.getTechnologies().size(); i++) {
            tex.append(i == 0 ? "" : ", ").append(resumeDto.getTechnologies().get(i));
        }
        tex.append("}\\\\\n" + "     \\textbf{Libraries}{: ");
        for (int i = 0; i < resumeDto.getLibraries().size(); i++) {
            tex.append(i == 0 ? "" : ", ").append(resumeDto.getLibraries().get(i));
        }
        tex.append("}\n" + "    }}\n\\end{itemize}\n\n\n" + "%-------------------------------------------\n" + "\\end{document}\n");
        return tex.toString();
    }

}
