package net.ronmichael.trial.model.dummy;

import net.ronmichael.trial.model.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static List<Message> ITEMS = new ArrayList<Message>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, Message> ITEM_MAP = new HashMap<String, Message>();

    static {
        // Add 3 sample items.
        addItem(new Message("id1", "Juan dela Cruz", "Lorem ipsum dolor sit amet, vestibulum tellus in aenean mi enim, mauris pellentesque pellentesque inceptos est, semper lacus. Id elit venenatis. Gravida donec nec ut, sagittis nulla maecenas sapien aenean, in a ut consequat donec nulla. In est elementum. Scelerisque maecenas potenti mollis in donec, sem justo, orci elit arcu lectus, donec etiam elit purus tellus fames. Eget ultrices vestibulum litora lobortis, nec eleifend tristique, ante tortor magna, tellus autem magna cras facilisis leo libero, ut laoreet facilisi cursus consequat metus. Lectus pede elit rutrum fringilla aliquam, ultricies condim", ""));
        addItem(new Message("id2", "Kaye Board", "Luctus mauris mauris sit purus viverra, non ac dictum molestie curabitur nulla, libero viverra nec inceptos magna malesuada nulla, amet eros, quis morbi suscipit praesent parturient. Suspendisse congue, nisl turpis amet est, sed consequat commodo mauris. Elementum tellus odio sit ultricies, vestibulum aliquam in a, ante et ante congue elit vitae nam, maece", ""));
        addItem(new Message("id3", "Monn Itor", "Metus metus auctor elementum malesuada a, lorem massa dolor commodo amet dui, massa feugiat consequuntur nulla, ut vel suscipit sed nonummy donec. Pretium fusce fusce, facilisi arcu mus molestie, nostra rutrum congue. Lobortis ornare sit, arcu elit lorem cras ac sodales, phasellus integer amet lectus massa, arcu pede facilisis nisl tristique nunc vestibulum. Plac", ""));
        addItem(new Message("id4", "Charlie Chicken", "Lorem ipsum dolor sit amet, vestibulum tellus in aenean mi enim, mauris pellentesque pellentesque inceptos est, semper lacus. Id elit venenatis. Gravida donec nec ut, sagittis nulla maecenas sapien aenean, in a ut consequat donec nulla. In est elementum. Scelerisque maecenas potenti mollis in donec, sem justo, orci elit arcu lectus, donec etiam elit purus tellus fames. Eget ultrices vestibulum litora lobortis, nec eleifend tristique, ante tortor magna, tellus autem magna cras facilisis leo libero, ut laoreet facilisi cursus consequat metus. Lectus pede elit rutrum fringilla aliquam, ultricies condim", ""));
        addItem(new Message("id5", "Mac Donalds", "Luctus mauris mauris sit purus viverra, non ac dictum molestie curabitur nulla, libero viverra nec inceptos magna malesuada nulla, amet eros, quis morbi suscipit praesent parturient. Suspendisse congue, nisl turpis amet est, sed consequat commodo mauris. Elementum tellus odio sit ultricies, vestibulum aliquam in a, ante et ante congue elit vitae nam, maece", ""));
        addItem(new Message("id6", "Jo Libee", "Metus metus auctor elementum malesuada a, lorem massa dolor commodo amet dui, massa feugiat consequuntur nulla, ut vel suscipit sed nonummy donec. Pretium fusce fusce, facilisi arcu mus molestie, nostra rutrum congue. Lobortis ornare sit, arcu elit lorem cras ac sodales, phasellus integer amet lectus massa, arcu pede facilisis nisl tristique nunc vestibulum. Plac", ""));
        addItem(new Message("id7", "Kaye Efcee", "Lorem ipsum dolor sit amet, vestibulum tellus in aenean mi enim, mauris pellentesque pellentesque inceptos est, semper lacus. Id elit venenatis. Gravida donec nec ut, sagittis nulla maecenas sapien aenean, in a ut consequat donec nulla. In est elementum. Scelerisque maecenas potenti mollis in donec, sem justo, orci elit arcu lectus, donec etiam elit purus tellus fames. Eget ultrices vestibulum litora lobortis, nec eleifend tristique, ante tortor magna, tellus autem magna cras facilisis leo libero, ut laoreet facilisi cursus consequat metus. Lectus pede elit rutrum fringilla aliquam, ultricies condim", ""));
        addItem(new Message("id8", "Pete Zahat", "Luctus mauris mauris sit purus viverra, non ac dictum molestie curabitur nulla, libero viverra nec inceptos magna malesuada nulla, amet eros, quis morbi suscipit praesent parturient. Suspendisse congue, nisl turpis amet est, sed consequat commodo mauris. Elementum tellus odio sit ultricies, vestibulum aliquam in a, ante et ante congue elit vitae nam, maece", ""));
        addItem(new Message("id9", "Shaye Keys", "Metus metus auctor elementum malesuada a, lorem massa dolor commodo amet dui, massa feugiat consequuntur nulla, ut vel suscipit sed nonummy donec. Pretium fusce fusce, facilisi arcu mus molestie, nostra rutrum congue. Lobortis ornare sit, arcu elit lorem cras ac sodales, phasellus integer amet lectus massa, arcu pede facilisis nisl tristique nunc vestibulum. Plac", ""));
    }

    private static void addItem(Message item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
