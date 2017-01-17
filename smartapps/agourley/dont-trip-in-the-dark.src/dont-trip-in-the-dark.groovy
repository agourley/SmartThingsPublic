/**
 *  Don't Trip in the Dark
 *
 *  Copyright 2017 Aaron Gourley
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Don't Trip in the Dark",
    namespace: "agourley",
    author: "Aaron Gourley",
    description: "Turns on the lights when motion is detected and its dark",
    category: "Safety & Security",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("When this sensor says it's dark:") {
    	input "thelightsensors", "capability.illuminanceMeasurement", required: true, title: "Where is it dark?", multiple: true
    }
    
    section("How dark is dark?") {
         input "dark", "number", required: true, title: "How dark (lux)?", defaultValue: 5
    }
    
    section("Turn on when motion detected:") {
		input "themotions", "capability.motionSensor", required: true, title: "Where is there motion?", multiple: true
	}
    
	section("Turn on this light:") {
		input "theswitches", "capability.switch", required: true, multiple: true
	}
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(themotions, "motion.active", motionDetectedHandler)
}

def motionDetectedHandler(evt) {
    log.debug "motionDetectedHandler called: $evt"
    
    for (thelightsensor in thelightsensors) {
    	log.debug "$thelightsensor reports: $thelightsensor.currentIlluminance"
    	if (thelightsensor.currentIlluminance < dark) {
    		theswitches.on()
    	}
    }
}
