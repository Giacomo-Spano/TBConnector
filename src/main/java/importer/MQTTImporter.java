package importer;

public class MQTTImporter extends Importer {

    public MQTTImporter(Importer importer) {
        super(importer);
    }

    public void init() {
        MQTTImporterTopicSubscriber topicSubscriber = new MQTTImporterTopicSubscriber(gethost(),getUser(),getPassword());
        topicSubscriber.init(gethost(),getUser(),getPassword());
    }
}
