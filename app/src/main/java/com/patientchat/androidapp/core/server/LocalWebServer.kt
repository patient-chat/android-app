package com.patientchat.androidapp.core.server

import fi.iki.elonen.NanoHTTPD

class LocalWebServer(port: Int) : NanoHTTPD(port) {
    override fun serve(session: IHTTPSession): Response {
        var msg = "<html><body><h1>Hello Phone #2</h1>\n"
        val parms = session.parms
        msg += if (parms["username"] == null) {
            """<form action='?' method='get'>
    <p>This is coming from my cell phone, direct, to you!</p>
        </form>
"""
        } else {
            "<p>Hello, " + parms["username"] + "!</p>"
        }
        return newFixedLengthResponse("$msg</body></html>\n")
    }
}