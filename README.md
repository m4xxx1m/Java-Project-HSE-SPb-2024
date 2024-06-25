<p align="middle">
  <img src="https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/client/composeApp/src/desktopMain/resources/icons/internfon_logo.png" height="200" hspace="7" />
</p>

Internfon is a multi-platform social network designed specifically for IT interns. This project aims to provide a dedicated space for interns to share posts, connect with peers, and create and showcase their resumes. It combines features of popular social networks with the specific needs of interns in the IT industry.

## Project Overview
### Functionalities
- Post Creation and Interaction: Users can create, rate, and comment on posts.
- Post Filtering and Search: Posts can be filtered by tags, and users can search posts.
- Favorites: Users can save and view favorite posts.
- User Interaction: Users can follow each other.
- Resume Creation: Users can create resumes using a special form.
- Resume Attachment: Users can attach resumes to posts either from their profile or as attached files.

Screenshots of the desktop version:

<p align="middle">
  <img src="https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/screenshots/post_with_comments.jpg" height="300" hspace="8" />
  <img src="https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/screenshots/profile.jpg" height="300" hspace="8" />
  <img src="https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/screenshots/subscriptions.jpg" height="300" hspace="8" /> 
  <img src="https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/screenshots/cv_example.jpg" height="300" hspace="8" />
</p>

## Installation and Setup
### Server
* Clone this repo
* Insert your database parameters into [application.properties](https://github.com/m4xxx1m/Java-Project-HSE-SPb-2024/blob/main/server/src/main/resources/application.properties)
* Create *.env file and insert this text: "TOKEN_SIGNING_KEY={your key}". Key can be generated via online resources (for example [here](https://generate-random.org/encryption-key-generator?count=1&bytes=32&cipher=aes-256-cbc&string=&password=))
* The server uses pdflatex to generate CV. Make sure you have a LaTeX distribution installed that includes pdflatex. For example, you can use TeX Live or MiKTeX, depending on your operating system.
* Run server
