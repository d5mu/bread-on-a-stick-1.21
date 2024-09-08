// BreadOnAStick.java
package d5mu.breadstick;

import d5mu.breadstick.registry.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreadOnAStick implements ModInitializer {
	public static final String MOD_ID = "bread_on_a_stick";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(itemGroup -> itemGroup.add(ModItems.BREAD_ON_A_STICK));

		ModItems.registerItems();
		LOGGER.info("Bread On A Stick Mod");
	}
}
