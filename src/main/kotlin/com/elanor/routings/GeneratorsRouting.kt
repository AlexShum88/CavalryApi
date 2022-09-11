package com.elanor.routings

import com.elanor.controllers.GeneratorsController
import com.elanor.controllers.ItemsController
import com.elanor.dao.model.entity.Generator
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.generatorsRouting() {
    route("generators") {

        get {
            GeneratorsController(call).getGeneratorsFacade()
        }
        getAllGenerators()
        insertGenerator()//
//        selectGeneratorById()
        selectGeneratorByName()
        selectGeneratorsByAuthor()
        selectGeneratorsByTheme()
        editGenerator()//
        itemsOfGenerator()
        itemFromGenerator()
    }
}


fun Route.getAllGenerators(){
    get("all") {
        GeneratorsController(call).getAllGenerators()
    }
}

//fun Route.selectGeneratorById() {
//    get("byId") {
//        GeneratorsController(call).selectGeneratorById()
//    }
//}

private fun Route.editGenerator(){
    route("edit") {
        route("generator") {
            post("update") {
                GeneratorsController(call).editGenerator()
            }
            delete("delete") {
                GeneratorsController(call).deleteGenerator()
            }
        }
        route("items") {
            post("updateMany") {
                ItemsController(call).updateAll()
            }
            post("update") {
                ItemsController(call).update()
            }
            post("insert") {
                ItemsController(call).insertItem()
            }
            delete("delete") {
                ItemsController(call).deleteItemsByGeneratorIdAndGrain()
            }
        }
    }
}

private fun Route.insertGenerator() {
    post("insert") {
        GeneratorsController(call).insertGenerator()
    }
}

private fun Route.selectGeneratorByName() {
    get("byName") {
        GeneratorsController(call).selectGeneratorByName()
    }
}

private fun Route.selectGeneratorsByAuthor() {
    get("byAuthor") {
        GeneratorsController(call).selectGeneratorsByAuthor()
    }
}

private fun Route.selectGeneratorsByTheme() {
    get("byTheme") {
        GeneratorsController(call).selectGeneratorsByTheme()
    }
}

private fun Route.itemsOfGenerator(){
    get ("allItems"){
        ItemsController(call).getItemsByGeneratorId()
    }
}

private fun Route.itemFromGenerator(){
    get ("oneItem"){
        ItemsController(call).getItemByGeneratorIdAndGrain()
    }
}

