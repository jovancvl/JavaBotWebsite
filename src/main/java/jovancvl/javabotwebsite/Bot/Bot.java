package jovancvl.javabotwebsite.Bot;

import jovancvl.javabotwebsite.Bot.managers.Listener;
import dev.arbjerg.lavalink.client.*;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Bot {
    //private static final Logger LOG = LoggerFactory.getLogger(Bot.class);

    //private static final int SESSION_INVALID = 4006;

    @Autowired
    LavalinkClient lavalinkClient;

    @Autowired
    Listener listener;

    public void startBot() throws InterruptedException {

        //String token = System.getenv("BotToken");

        //LavalinkClient lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(token));

        //Listener listener = new Listener();

        JDA jda = JDABuilder.createDefault(Constants.botToken)
                .setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavalinkClient))
                .addEventListeners(listener)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setActivity(Activity.customStatus("Now with a website!"))
                .build().awaitReady();

        jda.updateCommands()
                .addCommands(Commands.slash("ping", "Calculate ping of the bot"))
                .addCommands(Commands.slash("join", "Joins the voice chat you're in"))
                .addCommands(Commands.slash("leave", "Leaves the voice chat"))
                .addCommands(Commands.slash("play", "Plays or queues a song").addOption(OptionType.STRING, "query", "URL or name of the song", true))
                .addCommands(Commands.slash("queue", "Shows queue"))
                .addCommands(Commands.slash("skip", "Go to next song"))
                .addCommands(Commands.slash("stop", "Stops playback and clears queue"))
                .queue();
    }
}
