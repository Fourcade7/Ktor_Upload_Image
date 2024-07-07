package com.example.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }


        post("/upload") {
            val multipart = call.receiveMultipart()
            var fileDescription: String? = null
            var fileName: String? = null
            var fileBytes: ByteArray? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "description") {
                            fileDescription = part.value
                            
                        }
                    }
                    is PartData.FileItem -> {
                        fileName = part.originalFileName
                        fileBytes = part.streamProvider().readBytes()
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (fileBytes != null && fileName != null) {
                val file = File("uploads/$fileName")
                file.parentFile.mkdirs()
                file.writeBytes(fileBytes!!)
                call.respondText("File uploaded successfully: $fileDescription")
            } else {
                call.respondText("File upload failed")
            }
        }




//        post("/upload") { _ ->
//            // retrieve all multipart data (suspending)
//            val multipart = call.receiveMultipart()
//            multipart.forEachPart { part ->
//                // if part is a file (could be form item)
//                if(part is PartData.FileItem) {
//                    // retrieve file name of upload
//                    val name = part.originalFileName!!
//                    val file = File("/uploads/$name")
//
//                    // use InputStream from part to save file
//                    part.streamProvider().use { its ->
//                        // copy the stream to the file with buffering
//                        file.outputStream().buffered().use {
//                            // note that this is blocking
//                            its.copyTo(it)
//                        }
//                    }
//                }
//                // make sure to dispose of the part after use to prevent leaks
//                part.dispose()
//            }
//        }





//        post("/add-image") {
//            val multipart = call.receiveMultipart()
//            var fileName: String? = null
//            var text: String?= null
//            try{
//                multipart.forEachPart { partData ->
//                    when(partData){
//                        is PartData.FormItem -> {
//                            //to read additional parameters that we sent with the image
//                            if (partData.name == "text"){
//                                text = partData.value
//                            }
//                        }
//                        is PartData.FileItem ->{
//                            fileName = partData.save(Constants.USER_IMAGES_PATH)
//                        }
//                        is PartData.BinaryItem -> Unit
//                    }
//                }
//
//                val imageUrl = "${Constants.BASE_URL}/uploaded_images/$fileName"
//                call.respond(HttpStatusCode.OK,imageUrl)
//            } catch (ex: Exception) {
//                File("${Constants.USER_IMAGES_PATH}/$fileName").delete()
//                call.respond(HttpStatusCode.InternalServerError,"Error")
//            }
//        }




        /*
        *
        *
        *  post("/add-fruit") {
        try {

            // receive multipart data from the client
            val multipart = call.receiveMultipart()

            // define variable to hold our parameters data
            var fileName: String? = null
            var name: String? = null
            var season: String? = null
            val countries: MutableList<String> = mutableListOf()
            var imageUrl: String? = null

            try {
                // loop through each part of our multipart
                multipart.forEachPart { partData ->
                    when (partData) {
                        is PartData.FormItem -> {
                            // to read parameters that we sent with the image
                            when (partData.name) {
                                "name" -> name = partData.value
                                "season" -> season = partData.value
                                "countries" -> countries.add(partData.value)
                            }
                        }

                        is PartData.FileItem -> {
                            // to read the image data we call the 'save' utility function passing our path
                            if (partData.name == "image") {
                                fileName = partData.save(Constants.FRUIT_IMAGE_PATH)
                                imageUrl = "${Constants.BASE_URL}${Constants.EXTERNAL_FRUIT_IMAGE_PATH}/$fileName"
                            }
                        }

                        else -> Unit
                    }
                }
            } catch (ex: Exception) {
                // something went wrong with the image part, delete the file
                File("${Constants.FRUIT_IMAGE_PATH}/$fileName").delete()
                ex.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, "Error")
            }
            // create a new fruit object using data we collected above
            val newFruit = Fruit(
                name = name!!,
                // the valueOf function will find the enum class type that matches this string and return it, an Exception is thrown if the string does not match any type
                season = Fruit.Season.valueOf(season!!),
                countries = countries,
                image = imageUrl
            )
            // add the received fruit to the database
            if (!addFruit(newFruit)) {
                // if not added successfully return with an error
                return@post call.respond(
                    HttpStatusCode.Conflict,
                    SimpleResponse(success = false, message = "Item already exits")
                )
            }

            // acknowledge that we successfully added the fruit by responding
            call.respond(HttpStatusCode.Created, newFruit)
        } catch (ex: Exception) {

            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Invalid data"))
        }

    }
        *
        * */

    }
}
