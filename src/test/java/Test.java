import org.json.JSONObject;
import ru.dev.Bot;
import ru.dev.command.Command;
import ru.dev.context.MessageContext;

class Test {
    Bot bot = new Bot("ODAzNzQ2NDk0NDY1MDI0.fjitdbtspIG2FZvgEt3awkEBjejqFyPfMVT5kNVT20Q");
    @org.junit.jupiter.api.Test
    void repeat(){
        bot.getCommandManager().register(new Command("/repeat"){
            @Override
            public void onCommand(MessageContext ctx){
                if (ctx.getReferencedMessage() != null){
                    bot.getApiClient().sendMessage(ctx.getChannelId(),ctx.getReferencedMessage().getContent());
                    return;
                }
                bot.getApiClient().sendMessage(ctx.getChannelId(),"Ответь на сообщение ,которое надо повторить!");
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
