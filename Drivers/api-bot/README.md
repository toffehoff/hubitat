# SwitchBot - Bot

## Summary
Hubitat Driver to control a SwitchBot Bot using the SwitchBotAPI.

You can only use the SwitchBotAPI for Bot devices when they are enabled for Cloud Services via SwitchBot Hub.
You will need an SwitchBotAPI Open Token and know the DeviceID of the bot you'd like to control.

The driver support Bots in Press and Switch mode

## Getting an Open Token
You need to have an account on SwitchBot.com and the SwitchBot app installed on your phone.
* On your phone open the SwitchBot app
* Go to Profile > preference
* Tap App Version 10 times. The menu option Developer Options will show up
* Tap Developer Options
* Tap Get Token

## Getting the DeviceID
You need to get an Open Token first.
Open a command-prompt on your computer and enter the following command

```bash
curl -H "Authorization:<OPENTOKEN>" https://api.switch-bot.com/v1.0/devices
```
This will return a list of all your devices in JSON format including devicedId

## Compatibility
Tested on Hubitat Elevation 7 hardware with firmware version 2.2.4.158
