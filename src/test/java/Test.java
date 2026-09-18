import org.json.JSONObject;
import ru.dev.Bot;
import ru.dev.command.Command;
import ru.dev.context.MessageContext;

class Test {
    Bot bot = new Bot("ODAzNzQ2NDk0NDY1MDI0.s6T5zTO5QACyPpF3sQ166U-Srvoh9gwi68lxucMUAQA");
    @org.junit.jupiter.api.Test
    void command(){
        bot.getCommandManager().register(new Command("/test"){
            @Override
            public void onCommand(MessageContext messageContext){
                bot.getApiClient().sendMessage(messageContext.getChannelId(),"123");
            }
        } );

        bot.start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @org.junit.jupiter.api.Test
    void event(){

        bot.getEventPublisher().globalEvent(event -> {
            System.out.println("Event");
        });

        bot.getEventPublisher().messageCreateEvent(event -> {
            System.out.println(event.getType());
        });

        bot.getEventPublisher().readyEvent(event -> {
            System.out.println("bot ready");
        });

        bot.start();

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
