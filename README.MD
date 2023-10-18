# aMessaging

## aMessaging is a simple wrapper for the BungeeCord Plugin Messaging Channel.

### Example Usage:

```java

public class Example extends JavaPlugin {
    @Override
    public void onEnable() {
        // Initialize BungeeChannel
        final BungeeChannel bungeeChannel = new BungeeChannel(this);
        bungeeChannel.init();

        // Subscribe a subchannel
        bungeeChannel.subscribe("add_coins_channel", args -> {
            final Player player = args.getPlayer();
            final ByteArrayDataInput dataInput = args.getDataInput();

            // Do something with the data
        });

        // Send a message
        bungeeChannel.sendMessage(
                BungeeInputArgs.builder()
                        .player(player)
                        .messageAction(new ForwardAction(ForwardAction.SERVER_ALL, "add_coins_channel", Arrays.asList("Approximations", 1500)))
                        .build()
        );
    }
}
```

Planned TODO:

- [ ]  implement a better api for receiving messages ( BungeeCallbackArgs )