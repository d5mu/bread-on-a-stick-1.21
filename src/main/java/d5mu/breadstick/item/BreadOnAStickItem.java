package d5mu.breadstick.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.util.ActionResult;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

public class BreadOnAStickItem extends Item {

    // 첫 번째 주민 저장
    private VillagerEntity firstVillager = null;

    public BreadOnAStickItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);

        // 플레이어가 빵 낚싯대를 들고 있을 때 주민을 유혹
        if (!world.isClient) {
            double range = 8.0; // 유혹 거리 설정
            for (LivingEntity entity : world.getEntitiesByClass(LivingEntity.class, player.getBoundingBox().expand(range), e -> e instanceof VillagerEntity)) {
                VillagerEntity villager = (VillagerEntity) entity;
                villager.getNavigation().startMovingTo(player, 1.0); // 주민이 플레이어를 따라오도록 설정
            }
            System.out.println("주민이 플레이어를 따라오도록 설정했습니다.");
        }

        return TypedActionResult.success(itemStack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if (!player.getWorld().isClient && entity instanceof VillagerEntity villager) {

            if (!villager.isBaby()) { // 성체 주민 확인
                if (firstVillager == null) {
                    // 첫 번째 주민 설정
                    firstVillager = villager;
                    villager.setBreedingAge(0); // 번식 가능 상태로 설정
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_VILLAGER_CELEBRATE, SoundCategory.NEUTRAL, 1.0F, 1.0F);
                    System.out.println("첫 번째 주민이 설정되었습니다: " + firstVillager);
                } else {
                    // 두 번째 주민 설정 및 번식 처리
                    if (firstVillager != villager) {
                        villager.setBreedingAge(0); // 두 번째 주민 번식 가능 상태로 설정
                        firstVillager.setBreedingAge(0); // 첫 번째 주민도 번식 가능 상태로 설정
                        System.out.println("두 번째 주민이 설정되었습니다: " + villager);

                        // 번식 처리
                        attemptBreed((ServerWorld) player.getWorld(), firstVillager, villager);

                        // 내구도 감소
                        // stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));

                        // 첫 번째 주민 초기화
                        firstVillager = null;

                        return ActionResult.SUCCESS;
                    }
                }
            }
        }

        return ActionResult.PASS;
    }

    // 번식 로직
    private void attemptBreed(ServerWorld world, VillagerEntity villager1, VillagerEntity villager2) {
        // 번식 처리 로직
        villager1.setBreedingAge(12000); // 쿨다운 시간 설정
        villager2.setBreedingAge(12000); // 쿨다운 시간 설정

        System.out.println("번식이 성공적으로 완료되었습니다.");
    }
}
