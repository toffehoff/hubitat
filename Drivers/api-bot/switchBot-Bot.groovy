/*
 * SwitchBot Open API - Bot
 *
 * Uses the SwitchBotAPI to control a SwitchBot bot.
 *
 * Prerequisites:
 * - The SwitchBot needs to be cloud enable via a hub to make this work.
 * - You need to have a OpenToken from SwitchBot
 * - You need to know the DeviceID of the Bot you would like to control
 *
 */

metadata {
    definition(name: "SwitchBot Bot - API", namespace: "toffehoff", author: "ToffeHoff", importUrl: "https://raw.githubusercontent.com/toffehoff/hubitat/main/Drivers/api-bot/switchBot-Bot.groovy") {
        capability "Switch"
        capability "Actuator"
    }
}

preferences {
    section("URIs") {
        input name: "openToken", type: "string", title: "Your SwitchBot Open Token", required: true
        input name: "deviceId", type: "string", title: "Device ID of the bot", required: true
        input name: "switchBotMode", type: "enum", title: "Device mode of the bot", options: ["Press","Switch"], defaultValue: "Press", required: true
        input name: "logEnable", type: "bool", title: "Enable debug logging", defaultValue: true
    }
}

def logsOff() {
    log.warn "debug logging disabled..."
    device.updateSetting("logEnable", [value: "false", type: "bool"])
}

def updated() {
    log.info "updated..."
    log.warn "debug logging is: ${logEnable == true}"
    if (logEnable) runIn(1800, logsOff)
}

def parse(String description) {
    if (logEnable) log.debug(description)
}

def on() {

  if ( device.currentValue("switch") == "on" ) {
    if (logEnable) log.debug "Getting on request - Switch already on - no action required"
  } else {

      def postBody = '{ "command":"press" }'
      if ( switchBotMode == "Switch" ) postBody = '{ "command":"turnOn" }'

      if (logEnable) log.debug "Sending on POST request to device [${deviceId}] with message [${postBody}]"

      try {
          httpPost([
              uri: "https://api.switch-bot.com/v1.0/devices/${deviceId}/commands",
              headers: [
                  "Content-type": "application/json; charset=utf8",
                  "Authorization": "${openToken}"
              ],
              body: postBody
          ]) { resp ->
              if (resp.success) {
                  sendEvent(name: "switch", value: "on", isStateChange: true)
              }
              if (logEnable)
                  if (resp.data) log.debug "${resp.data}"
          }
      } catch (Exception e) {
          log.warn "Call to on failed: ${e.message}"
      }
  }
}

def off() {

  if ( device.currentValue("switch") == "off" ) {

    if (logEnable) log.debug "Getting off request - Switch already off - no action required"

  } else {

    def postBody = '{ "command":"press" }'
    if ( switchBotMode == "Switch" ) postBody = '{ "command":"turnOff" }'

    if (logEnable) log.debug "Sending off POST request to device [${deviceId}] with message [${postBody}]"

    try {
        httpPost([
            uri: "https://api.switch-bot.com/v1.0/devices/${deviceId}/commands",
            headers: [
                "Content-type": "application/json; charset=utf8",
                "Authorization": "${openToken}"
            ],
            body: postBody
        ]) { resp ->
            if (resp.success) {
                sendEvent(name: "switch", value: "off", isStateChange: true)
            }
            if (logEnable)
                if (resp.data) log.debug "${resp.data}"
        }
    } catch (Exception e) {
        log.warn "Call to on failed: ${e.message}"
    }
  }
}
