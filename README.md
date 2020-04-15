# Patient Chat
# Local Peer-to-Peer Video Chat for Biosecurity Medical Contexts

For the moment this doc is a partial copy of a shared [Google doc](https://docs.google.com/document/d/1k-E5vsnHiFbQEnSc5N-PSwfyGaco9PoHPll9fLqPLSA/edit?usp=sharing).

## Overview
Phone-to-phone video chat without Internet or a Local Area Network needed.

A single-purpose application allowing high-quality audio and video chat between two nearby mobile devices without external networking. Essentially turning two phones into a pair of video-enabled "walkie-talkies", connected to one another by Wi-Fi signals (which have far better range than Bluetooth) without any other network or devices involved. 

Intended for use where biosecurity makes direct contact difficult and unsafe, allowing easier communication with patients inside biosecurity zones. 
Primary use-case (based on requests from medics) is a COVID-19 medic checking in frequently with a patient in an isolation room without physically visiting each time.

Medics can use it to interact more frequently with their patients while conserving PPE and minimizing exposure risk. Families can use it to interact with loved ones that cannot be safely visited in person. Works like a baby monitor, but deployed on any mobile device without additional hardware.

Open source and freely available to all users. Copyleft in hopes that improvements made by commercial contributors will be shared.

## Problem Statement
Medics caring for COVID-19 or Ebola patients must don Personal Protective Equipment to interact directly with their patients, and family members are often unable to interact directly with patients at all. Video chat does not replace in-person interaction, but can supplement it (or at least be the next best thing).

Hospitals in high-income countries often have poor wireless network connectivity where patients are being treated, particularly in areas with biosecurity measures in place. This makes most video chat applications—most of which rely upon the Internet—impractical. Some chat applications rely on Local Area Network (LAN) connectivity, and a few establish direct, peer-to-peer connections between mobile devices (baby monitor apps seem to be the main category that do so, but use a LAN with P2P bluetooth as a fallback; Bluetooth doesn't perform well in clinical settings due to its very short range and sensitivity to physical barriers). However, we have not found any that establish a simple, robust direct Wi-Fi connection between two devices without use of the Internet or LAN.

## Proposed Application
A two part system. For the purposes of establishing connections, one mobile device functions as the server, the other as the client.

Both devices present a simple audio and video chat window to their users. The server device user initiates the and can terminate the chat, but while running both users have essentially the same experience of a two-way video conference.

The server device creates a Wi-Fi hotspot with an SSID specified by and identifying both users ("Dr Kelly for Robin", for example).

The client device user (most likely a patient) logs onto the hotspot using the SSID and a password provided by the server device user.

Ideally they are immediately prompted to "log into the wireless network" in the same way cafe or airport hotspots use a captive portal.

The server device application launches a video chat window.

The client device user opens a browser or app window (ideally from the captive portal window) and is presented with a full-screen video chat.

## Various Architectural Considerations
The server device needs to create a hotspot and manage connections. Since it will be used with multiple clients, it should create different SSIDs for each to avoid confusion and congestion.
Update: there are peer-to-peer frameworks for this, creating a hotspot may not be necessary. 
https://developer.android.com/guide/topics/connectivity/wifip2p
https://developer.apple.com/documentation/multipeerconnectivity

Good quality video chat minimizes latency, and therefore should not waste time and data attempting to ensure 100% packet transmission success (bandwidth and low latency trump completeness; a few dropouts are more tolerable than noticeable delays). Therefore User Datagram Protocol (UDP) is probably a better choice than Transmission Control Protocol (TCP). If both the server and the client are running apps, this should be easy. If the client is using a browser to connect (which would certainly make deployment easier in a hospital; most patients will already have a device with a browser installed), it may be possible to use QUIC for transmission of the actual video chat data.

It is always tempting to add functionality (one of the medics we've discussed this with has already suggested adding a form for the patient to pre-fill outlining their main concerns before the chat). However, we firmly believe that a very simple, one-feature-only application is the appropriate model for the circumstances. We build a razor blade, not a Swiss Army knife. High-quality audio and video chat between two phones, developed freely, implemented quickly, and working smoothly straight outta the box. Nothing more. 

## Security and Privacy
The primary security is simply that it's not networked beyond the two devices. To listen in, an attacker would need to be physically close enough to receive the radio transmissions from the devices (and have cracked the SSL-style encryption) or have remote control of one of the devices via a Trojan or other malware.

An attacker sufficiently motivated and sophisticated to eavesdrop on the direct radio connection between the devices would probably be better off simply buying a directional microphone (which as an added bonus would allow them to eavesdrop on any other conversation as well since they're already in the hospital).

If either party's mobile device has been hacked to the point that someone can listen in on their video chats, they're probably also hacked to the point that all phone calls and even in-person conversations are exposed as well.

Nevertheless, we take measures:
- The server device allows only one device onto the hotspot. If another device manages to log on, the video chat is terminated and a warning is displayed.
- The server app reconfigures the hotspot to provide a different SSID and password for every client user.
- The individual packets are encrypted using Secure Socket Layer (SSL) style encryption; this provides plenty of protection from casual eavesdropping without significantly increasing bandwidth or latency (at the cost of a bit of local processing on both devices, but nothing huge).

## Performance
The basic reason this application is expected to perform better than Zoom, Skype, Hangouts, or a baby monitor is that it's not using any shared network. No Internet, no LAN, just a strictly one-to-one Wi-Fi connection between two devices. 

Obviously if the Wi-Fi radio bands (2.4 and 5GHz) are saturated by other network and device use this won't really help, but as long as there are Wi-Fi radio channels available (probably usually the case in most clinical settings) it should work better than any other alternative short of a dedicated video conferencing LAN device (which also may be a good idea but is much more complicated for users to deploy).

However, even assuming decent bandwidth, it's important to tweak performance to be as fit as possible for purpose.

As with most video conferencing applications, audio is more sensitive to dropouts and should be prioritized when bandwidth drops (better to have a frozen picture and clear audio than both video and audio degraded).

Video bandwidth should be adjusted for bandwidth. If a patient is in a well-shielded or electromagnetically noisy room, far from their interlocutor, behind thick walls/doors, or simply during a moment of poor transmission, both resolution and frame rate should be dynamically adjusted to reduce data rates (and preserve audio if possible).

Compression setting should be tweaked to make the best possible use of the devices' local processing power. If someone has a phone with plenty of processing horsepower, it makes sense to compress the datastream more aggressively (trading local processing for data transmission bandwidth). However, this won't work well on an old or cheap device (or might introduce excessive latency), so should be dynamically adjusted.
