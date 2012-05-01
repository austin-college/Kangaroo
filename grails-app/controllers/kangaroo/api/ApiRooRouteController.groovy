package kangaroo.api

import grails.converters.JSON

class ApiRooRouteController {

    def stops = [
            "austinCollege": [name: "Austin College", longitude: 33.647368, latitude: -96.597601, description: null, funnyDescription: "Home of the Roo's!"],
            "zauk": [name: "Zauk Circle", longitude: 33.646598, latitude: -96.595556, description: null, funnyDescription: "Main route stop."],,
            "rooSuites": [name: "Roo Suites", longitude: 33.644470, latitude: -96.599118, description: null, funnyDescription: "Still on meal plan, sorry kids."],
            "flats": [name: "The Flats", longitude: 33.644403, latitude: -96.600610, description: null, funnyDescription: "Do they actually stop here?"],
            "gloryToGod": [name: "Slap Yo' Momma!", longitude: 33.645502, latitude: -96.605250, description: null, funnyDescription: "AKA Glory to God"],
            "tacoBell": [name: "Taco Bell", longitude: 33.654799, latitude: -96.606478, description: null, funnyDescription: "The cure for your soon to be hangover."],
            "kroger": [name: "Kroger", longitude: 33.656447, latitude: -96.606425, description: null, funnyDescription: "Kroger < HEB"],
            "cvs": [name: "CVS", longitude: 33.657081, latitude: -96.604354, description: null, funnyDescription: "Open 24 Hours, accessable by RooRoute for 8"],
            "cellarmans": [name: "Cellarman's", longitude: 33.661668, latitude: -96.599361, description: null, funnyDescription: "Heaven is for real."],
            "tacoBueno": [name: "Taco Bueno", longitude: 33.662412, latitude: -96.598882, description: null, funnyDescription: "Tacos, don't know if they're bueno or not."],
            "hastingsSubway": [name: "Hastings and Subway", longitude: 33.661492, latitude: -96.600105, description: null, funnyDescription: "Dirty magazines."],
            "oliveGarden": [name: "Olive Garden", longitude: 33.675663, latitude: -96.608265, description: null, funnyDescription: "Olives! A Garden! Microwaved pasta."],
            "whatabuger": [name: "Whataburger", longitude: 33.676315, latitude: -96.608174, description: null, funnyDescription: "Great at 4am. Shame the Roo Route doesn't run then."],
            "academy": [name: "Academy", longitude: 33.678774, latitude: -96.605738, description: null, funnyDescription: "Sports and outdoors, THE RIGHT STUFF, THE RIGHT PRICE."],
            "kohls": [name: "Kohl's", longitude: 33.679953, latitude: -96.605620, description: null, funnyDescription: "Like a department store, but just a regular store."],
            "chipotle": [name: "Chipotle", longitude: 33.682547, latitude: -96.608737, description: null, funnyDescription: "Infant-sized burritos."],
            "panera": [name: "Panera", longitude: 33.682546, latitude: -96.608754, description: null, funnyDescription: "Soup in a bread bowl."],
            "target": [name: "Target", longitude: 33.678556, latitude: -96.612276, description: null, funnyDescription: "Your target destination."],
            "booksAMillion": [name: "Books A Million", longitude: 33.677261, latitude: -96.612330, description: null, funnyDescription: "HOW MANY BOOKS DO YOU NEED?"],
            "cinemark": [name: "Cinemark", longitude: 33.675203, latitude: -96.611691, description: null, funnyDescription: "See GhostRider 5: We've Gone Too Far"],
            "walmart": [name: "Walmart", longitude: 33.673056, latitude: -96.612018, description: null, funnyDescription: "China*"]]

    def index = {
        render(stops as JSON);
    }
}
