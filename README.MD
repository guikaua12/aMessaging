# aMessaging

aMessaging is a simple wrapper for the BungeeCord Plugin Messaging Channel.

## Installation:

### Maven:

Repository:

```xml

<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Dependency:

```xml

<dependency>
    <groupId>com.github.guikaua12</groupId>
    <artifactId>aMessaging</artifactId>
    <version>v0.1.0</version>
</dependency>
```

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

        // Send a req/resp message
        bungeeChannel.sendReqRespMessage(BungeeInputArgs.builder()
                        .messageAction(new PlayerCountAction("lobby"))
                        .build(), String.class, Integer.class)
                .thenAccept(playerCount -> {
                    sender.sendMessage("there is " + playerCount + " players online on the lobby server");
                });
    }
}
```

Planned TODO:

- [ ]  implement a better api for receiving messages ( BungeeCallbackArgs )