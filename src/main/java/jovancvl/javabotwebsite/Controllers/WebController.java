package jovancvl.javabotwebsite.Controllers;

import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Controller
public class WebController {

    private static final Logger LOG = LoggerFactory.getLogger(WebController.class);

    @Autowired
    MusicManager musicManager;

    @GetMapping("")
    public String index(){
        LOG.info("Home page opened on {}", new Date());
        return "index";
    }

    @GetMapping("/controls/{server}")
    public String play(Model model, @PathVariable String server) {
        //System.out.println("controls accessed on server " + server);
        long guildId = Long.parseLong(server);
        model.addAttribute("server", guildId);
        String songName = "No song is playing";
        try {
            songName = musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getTrack().getInfo().getTitle();
        } catch (NullPointerException e){

        }

        List<String> songList = this.musicManager.getSongQueue(guildId).stream().map((track) -> track.getInfo().getTitle()).toList();


        model.addAttribute("song", songName);
        model.addAttribute("queue", songList);
        return "controls";
    }
}