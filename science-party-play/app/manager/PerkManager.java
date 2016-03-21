package manager;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.annotation.Transactional;
import models.ebean.Perk;
import models.ebean.PerkPerTopic;
import models.ebean.Topic;
import util.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * This class manages all the the methos related to the perks.
 */
public class PerkManager {

    public static void updatePerksPerTopic() {
        // To be fast force ebean to do all changes in one transaction
        Ebean.beginTransaction();
        try {
            List<Perk> perks = Perk.find.all();
            List<Topic> topics = Topic.find.all();
            List<String> md5hashs = new ArrayList<String>();

            // Let's check each possible entry for existence and relevance
            for (Perk perk : perks) {
                for (Topic topic : topics) {
                    // Generate alle possible md5s and add it to a list
                    String md5hash = Helper.getMD5fromString(perk.getName() + " " + topic.getName());
                    md5hashs.add(md5hash);
                    //Check if md5 hash is already in the table and do nothing if it already exists
                    if (PerkPerTopic.find.where().ieq("qrCode", "md5hash").findUnique() == null) {
                        //Check if there is a entry with the given combination
                        PerkPerTopic perkPerTopic = PerkPerTopic.find.where()
                                .ieq("perk", perk.getId().toString())
                                .ieq("topic", topic.getId().toString())
                                .findUnique();
                        if (perkPerTopic == null) {
                            //Combination does not exists so we can create it
                            perkPerTopic = new PerkPerTopic();
                            perkPerTopic.setPerk(perk);
                            perkPerTopic.setTopic(topic);
                            perkPerTopic.setQrCode(md5hash);
                            perkPerTopic.save();
                        } else {
                            //Combination does exists so we can update it
                            perkPerTopic.setQrCode(md5hash);
                            perkPerTopic.save();
                        }
                    }
                }
            }

            Ebean.commitTransaction();
        } finally {
            Ebean.endTransaction();
        }
    }
}
