package network

import kotlinx.serialization.Serializable

@Serializable
data class Education(
    val organization: String,
    val degree: String,
    val major: String,
    val location: String,
    val studyPeriod: String
)

@Serializable
data class Experience(
    val organization: String,
    val workPeriod: String,
    val position: String,
    val location: String,
    val tasks: List<String>
)

@Serializable
data class Project(
    val projectName: String,
    val workPeriod: String,
    val usedTechnologies: String,
    val tasks: List<String>
)

@Serializable
data class ResumeDto(
    val name: String,
    val surname: String,
    val contacts: List<String>,
    val educations: List<Education>,
    val experiences: List<Experience>,
    val projects: List<Project>,
    val programmingLanguages: List<String>,
    val frameworks: List<String>,
    val technologies: List<String>,
    val libraries: List<String>
)