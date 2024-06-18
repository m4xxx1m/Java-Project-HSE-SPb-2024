package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import model.AuthManager
import network.Education
import network.Experience
import network.Project
import network.ResumeDto
import network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ResumeForm(navigator: Navigator? = null) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var contacts by remember { mutableStateOf(listOf<String>()) }
    var educations by remember { mutableStateOf(listOf<Education>()) }
    var experiences by remember { mutableStateOf(listOf<Experience>()) }
    var projects by remember { mutableStateOf(listOf<Project>()) }
    var programmingLanguages by remember { mutableStateOf(listOf<String>()) }
    var frameworks by remember { mutableStateOf(listOf<String>()) }
    var technologies by remember { mutableStateOf(listOf<String>()) }
    var libraries by remember { mutableStateOf(listOf<String>()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = surname,
            onValueChange = { surname = it },
            label = { Text("Surname") },
            modifier = Modifier.fillMaxWidth()
        )

        Text("Contacts", style = MaterialTheme.typography.h6)
        DynamicListInput(items = contacts, onItemsChange = { contacts = it }, label = "Contact")

        Text("Educations", style = MaterialTheme.typography.h6)
        DynamicEducationList(educations) { educations = it }

        Text("Experiences", style = MaterialTheme.typography.h6)
        DynamicExperienceList(experiences) { experiences = it }

        Text("Projects", style = MaterialTheme.typography.h6)
        DynamicProjectList(projects) { projects = it }

        Text("Programming Languages", style = MaterialTheme.typography.h6)
        DynamicListInput(
            items = programmingLanguages,
            onItemsChange = { programmingLanguages = it },
            label = "Programming Language"
        )

        Text("Frameworks", style = MaterialTheme.typography.h6)
        DynamicListInput(
            items = frameworks,
            onItemsChange = { frameworks = it },
            label = "Framework"
        )

        Text("Technologies", style = MaterialTheme.typography.h6)
        DynamicListInput(
            items = technologies,
            onItemsChange = { technologies = it },
            label = "Technology"
        )

        Text("Libraries", style = MaterialTheme.typography.h6)
        DynamicListInput(items = libraries, onItemsChange = { libraries = it }, label = "Library")

        Button(onClick = {
            val resumeDto = ResumeDto(
                name = name,
                surname = surname,
                contacts = contacts,
                educations = educations,
                experiences = experiences,
                projects = projects,
                programmingLanguages = programmingLanguages,
                frameworks = frameworks,
                technologies = technologies,
                libraries = libraries
            )
//            val json = convertToJson(resumeDto)
            sendResume(resumeDto, navigator)
        }) {
            Text("Submit")
        }
    }
}

fun sendResume(resumeDto: ResumeDto, navigator: Navigator?) {
    RetrofitClient.retrofitCall.createResume(AuthManager.currentUser.id, resumeDto).enqueue(
        object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() == 200) {
                    navigator?.pop()
                } else {
                    println("wrong code on creating resume")
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                println("failure on creating resume")
            }
        }
    )
}

@Composable
fun DynamicListInput(
    items: List<String>,
    onItemsChange: (List<String>) -> Unit,
    label: String
) {
    Column {
        items.forEachIndexed { index, item ->
            OutlinedTextField(
                value = item,
                onValueChange = { newItem ->
                    onItemsChange(items.toMutableList().apply { set(index, newItem) })
                },
                label = { Text("$label ${index + 1}") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Button(onClick = {
            onItemsChange(items + "")
        }) {
            Text("Add $label")
        }
    }
}

@Composable
fun DynamicEducationList(
    educations: List<Education>,
    onEducationsChange: (List<Education>) -> Unit
) {
    Column {
        educations.forEachIndexed { index, education ->
            EducationInput(
                education = education,
                onEducationChange = { newEducation ->
                    onEducationsChange(
                        educations.toMutableList().apply { set(index, newEducation) })
                }
            )
        }
        Button(onClick = {
            onEducationsChange(educations + Education("", "", "", "", ""))
        }) {
            Text("Add Education")
        }
    }
}

@Composable
fun EducationInput(
    education: Education,
    onEducationChange: (Education) -> Unit
) {
    Column {
        OutlinedTextField(
            value = education.organization,
            onValueChange = { onEducationChange(education.copy(organization = it)) },
            label = { Text("Organization") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = education.degree,
            onValueChange = { onEducationChange(education.copy(degree = it)) },
            label = { Text("Degree") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = education.major,
            onValueChange = { onEducationChange(education.copy(major = it)) },
            label = { Text("Major") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = education.location,
            onValueChange = { onEducationChange(education.copy(location = it)) },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = education.studyPeriod,
            onValueChange = { onEducationChange(education.copy(studyPeriod = it)) },
            label = { Text("Study Period") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun DynamicExperienceList(
    experiences: List<Experience>,
    onExperiencesChange: (List<Experience>) -> Unit
) {
    Column {
        experiences.forEachIndexed { index, experience ->
            ExperienceInput(
                experience = experience,
                onExperienceChange = { newExperience ->
                    onExperiencesChange(
                        experiences.toMutableList().apply { set(index, newExperience) })
                }
            )
        }
        Button(onClick = {
            onExperiencesChange(experiences + Experience("", "", "", "", listOf()))
        }) {
            Text("Add Experience")
        }
    }
}

@Composable
fun ExperienceInput(
    experience: Experience,
    onExperienceChange: (Experience) -> Unit
) {
    Column {
        OutlinedTextField(
            value = experience.organization,
            onValueChange = { onExperienceChange(experience.copy(organization = it)) },
            label = { Text("Organization") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = experience.workPeriod,
            onValueChange = { onExperienceChange(experience.copy(workPeriod = it)) },
            label = { Text("Work Period") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = experience.position,
            onValueChange = { onExperienceChange(experience.copy(position = it)) },
            label = { Text("Position") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = experience.location,
            onValueChange = { onExperienceChange(experience.copy(location = it)) },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        DynamicListInput(
            items = experience.tasks,
            onItemsChange = { onExperienceChange(experience.copy(tasks = it)) },
            label = "Task"
        )
    }
}

@Composable
fun DynamicProjectList(
    projects: List<Project>,
    onProjectsChange: (List<Project>) -> Unit
) {
    Column {
        projects.forEachIndexed { index, project ->
            ProjectInput(
                project = project,
                onProjectChange = { newProject ->
                    onProjectsChange(projects.toMutableList().apply { set(index, newProject) })
                }
            )
        }
        Button(onClick = {
            onProjectsChange(projects + Project("", "", "", listOf()))
        }) {
            Text("Add Project")
        }
    }
}

@Composable
fun ProjectInput(
    project: Project,
    onProjectChange: (Project) -> Unit
) {
    Column {
        OutlinedTextField(
            value = project.projectName,
            onValueChange = { onProjectChange(project.copy(projectName = it)) },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = project.workPeriod,
            onValueChange = { onProjectChange(project.copy(workPeriod = it)) },
            label = { Text("Work Period") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = project.usedTechnologies,
            onValueChange = { onProjectChange(project.copy(usedTechnologies = it)) },
            label = { Text("Used Technologies") },
            modifier = Modifier.fillMaxWidth()
        )
        DynamicListInput(
            items = project.tasks,
            onItemsChange = { onProjectChange(project.copy(tasks = it)) },
            label = "Task"
        )
    }
}
