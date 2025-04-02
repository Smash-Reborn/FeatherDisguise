# FeatherDisguise
### A lightweight non-bullshit Super Smash Mobs disguise plugin for Minecraft 1.8.
[![Environment](https://img.shields.io/badge/environment-serverside-blue.svg)](https://shields.io/) [![MinecraftVersion](https://img.shields.io/badge/minecraftversion-1.8-blue.svg)](https://shields.io/) [![Release](https://img.shields.io/badge/release-1.1.0-blue.svg)](https://shields.io/) [![CodeStatus](https://img.shields.io/badge/status-stable-green.svg)](https://shields.io/) [![JarVersion](https://img.shields.io/badge/servertype-spigot-orange.svg)](https://shields.io/)
<br />
<br />

### Features:

> ✅ Completely NMS based disguise system that utilises either PacketEvents interception or a custom written EntityTracker.

> ✅ Perfectly synchronous entity tracking with no delayed packets or hacky schedulers bloating the main thread.

> ✅ Option to pick between using the custom EntityTracker or the PacketEvents interception system.

> ✅ Comes with commands for ease of use. Disguising, undisguising & refreshing disguises.

> ✅ A ground up written metadata handling system designed for easy setting & getting of entity data traits.

> ✅ Asynchronous melee attack handling. No re-synchronisation with the main thread or delayed melee packets.

> ✅ Custom EntityTracker has optimised methods and routines that not only improves general entity performance but also math performance for disguises and packet-work.

> ✅ All 1.8 LivingEntities are available to disguise as. (non-living coming eventually!)

> ✅ An easy to use API for applying, removing, refreshing and handling disguises.
<br />

### Comments:

This has been a long time coming. The journey started with my research into how disguises were handled back in ~2021 and ever since then, been improving and creating better and better systems. With the creation of Reborns ProjectQ (aka 2.0), the goal was to create a flexible and dynamic way to handle disguises without relying on flawed packet interception routes with libraries like protocolib or packetevents. That lead to a rewrite of the EntityTracker and its associated systems in order to make entity and disguise handling as streamlined and fast as possible. However, with the migration to modern minecraft, and ProjectOB1 (aka 3.0), replacing the EntityTracker was no longer possible due to the technical and code complexity of the modern game. Not to mention using forks like PaperSpigot means you have to go through years of patches and obfuscated NMS which continues to become more closed off as time goes on. While our modern disguise library (proprietary atm) was quite good at handling what it needed too, it too became a slave to the packet interception route due to the aformentioned reasons. 

My goals for this were to take the knowledge learned from all these years, as well as the systems I've created and roll them into something that's flexible, fast and has lots of options exclusively for our 1.8 codebases (and for the community). And while I am happy with the outcome currently, there are always improvements to be made. Because of the necessity to provide functionality for both situations (custom tracker & packet interception) this has lead to a bit of a bloat to some of the codebase. Although I think that's dismissable given the amount of utility and functionality the code provides. Furthermore I am really happy with the overall gains to performance the custom entity tracker provides. The fact the math and packet work can be rolled into one thing is just far superior to any form of
packet interception (without some sort of caching or custom inject implementation). 

Making this open source is mostly for the communities benefit. Many disguise plugins are not well documented, some have extreme boilerplate or are just not well done. Many of the processes behind the math, how packet math is calculated and how things such as metadata are constructed is not something you can just casually read about on the internet. I hope that through this, you can see my code, read my comments and come to a better understanding of things as I have. And of course, improve on what is here. I am a strong believer that all code can be improved, and this should never be the end of trying to make something better. 

The final thing I want to mention is the custom entity tracker route only works on versions of the game that still keep entity tracking separate from things like chunk tracking and world handling. I think from version ~1.16 and up it's completely different which makes entity tracker rewrites basically a non-viable solution. Of course the ultimate way to do this would be to directly patch the jar, completely removing the superclass entity tracker and replacing it with something completely new, but that also is not compad with other servers or libraries. For these reasons, is why this also provides the more widely used packet interception methods. 
<br />

### Q&A:

**?**  Where can I download this plugin?
> Check the **Releases** tab on the side of the page. There you should find a direct jar you can download. Alternatively, you can pull the code and compile yourself.

**?**  Can I use this as just a normal disguise plugin?
> Yes you can but it will have the hittable squid on top of the disguise at all times. It won't 100% parody other disguise plugins that are currently available but it'll do everything else perfectly.

**?**  I've found a bug/issue or I want to make a feature request? Where do I go?
> Head over to the **Issues** tab at the top of the page and fill out a new ticket. If it's a bug, please provide details, videos, screenshots or anything that can help identification and fixing easier.

**?**  How can I pull the codebase and make modifications myself?
> Go watch a tutorial on YT about setting up an IDE. Once you've got that, simply <ins>clone</ins> the codebase and open it with your IDE. Alternatively, if you want to make your own contributions or have your own version, <ins>fork</ins> the repo instead. Next, you should see a gradle buildscript option pop-up. (if it doesn't, press the "refresh gradle project" button) Let that process run until everything has finished. If you are still getting errors (particularly from the NMS code) you probably haven't run BuildTools, so you should go do that. (googling will provide you tutorials on how to do that)
<br />

> [!IMPORTANT]
> If you need help or want to see the plugin in action, join the official public discord server: [Smash Reborn](https://discord.gg/yPyGdjzSzw)
