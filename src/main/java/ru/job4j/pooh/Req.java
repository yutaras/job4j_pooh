package ru.job4j.pooh;

public class Req {

    private final String httpRequestType;
    private final String poohMode;
    private final String sourceName;
    private final String param;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String TOPIC = "topic";

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    public static Req of(String content) {
        String httpRequestType;
        String poohMode = null;
        String sourceName = null;
        String param = "";
        var args = content.split(System.lineSeparator());
        var partOne = args[0].split(" ");
        httpRequestType = partOne[0];
        if (POST.equals(httpRequestType)) {
            var partTwo = partOne[1].split("/");
            poohMode = partTwo[1];
            sourceName = partTwo[2];
            param = args[args.length - 1];
        }
        if (GET.equals(httpRequestType)) {
            var partTwo = partOne[1].split("/");
            poohMode = partTwo[1];
            sourceName = partTwo[2];
            if (TOPIC.equals(poohMode)) {
                param = partTwo[3];
            }
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String httpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
