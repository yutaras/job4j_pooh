package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String EMPTY_STATUS = "204";
    private static final String OK_STATUS = "200";

    @Override
    public Resp process(Req req) {
        String text = "";
        String status = null;
        if (GET.equals(req.httpRequestType())) {
            if (queue.get(req.getSourceName()) == null || queue.get(req.getSourceName()).isEmpty()) {
                status = EMPTY_STATUS;
            } else {
                text = queue.get(req.getSourceName()).poll();
                status = OK_STATUS;
            }
        } else if (POST.equals(req.httpRequestType())) {
            if (queue.get(req.getSourceName()) == null) {
                queue.putIfAbsent(req.getSourceName(), new ConcurrentLinkedQueue<>());
            }
            text = req.getParam();
            queue.get(req.getSourceName()).add(text);
            status = OK_STATUS;
        }
        return new Resp(text, status);
    }
}
