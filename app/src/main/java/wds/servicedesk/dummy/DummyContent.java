package wds.servicedesk.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.CustomerDataSource;

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
    //public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final List<CustomerDataSource.Customer> ITEMS = new ArrayList<CustomerDataSource.Customer>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    //public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();
    public static final Map<String, CustomerDataSource.Customer> ITEM_MAP = new HashMap<String, CustomerDataSource.Customer>();

    private static final int COUNT = 5;

    static {
                // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    //private static void addItem(DummyItem item) {
        private static void addItem(CustomerDataSource.Customer item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

   // private static DummyItem createDummyItem(int position) {
   private static CustomerDataSource.Customer createDummyItem(int position) {
      //  return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
      // return new CustomerDataSource.Customer(String.valueOf(position), "Item " + position, makeDetails(position));
       return null;
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
        public static class DummyItem {
        public final String id;
        public final String content;
        public final String details;

        public DummyItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
