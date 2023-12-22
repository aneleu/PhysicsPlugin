package me.aneleu.physicsplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;

import java.util.List;

import static me.aneleu.physicsplugin.PhysicsPlugin.tickPerCalculation;

public class DisplayObject {

    static final List<double[]> VERTEXES = List.of(
            new double[]{-0.5, -0.5, -0.5},
            new double[]{-0.5, -0.5, 0.5},
            new double[]{-0.5, 0.5, -0.5},
            new double[]{-0.5, 0.5, 0.5},
            new double[]{0.5, -0.5, -0.5},
            new double[]{0.5, -0.5, 0.5},
            new double[]{0.5, 0.5, -0.5},
            new double[]{0.5, 0.5, 0.5}
    );
    static final double GRAVITY = 0.098;

    final BlockDisplay object;

    World world;

    double x, y, z;
    double[] rightRotation = new double[]{0.7071, 0.7071, 0.7071, Math.toRadians(45)};
    double[] leftRotation = new double[]{0, 0, 1, 0};
    double rotationSpeed = 0;
    Vector velocity = new Vector(0, 0, 0);

    public DisplayObject(Location location, String block) {
        world = location.getWorld();
        location.setPitch(0);
        location.setYaw(0);
        object = (BlockDisplay) world.spawnEntity(location, EntityType.BLOCK_DISPLAY);
        object.setBlock(Bukkit.getServer().createBlockData(block));

        x = location.getX();
        y = location.getY();
        z = location.getZ();

        double[] rightRotationTranslation = RotateUtil.rotateDotByVector(new double[]{0.5, 0.5, 0.5}, new double[]{rightRotation[0], rightRotation[1], rightRotation[2]}, rightRotation[3]);

        Transformation transformation = object.getTransformation();
        transformation.getRightRotation().setAngleAxis(rightRotation[3], rightRotation[0], rightRotation[1], rightRotation[2]);
        transformation.getTranslation().set(-rightRotationTranslation[0], -rightRotationTranslation[1], -rightRotationTranslation[2]);
        object.setTransformation(transformation);

    }

    public void move() {

        velocity.setY(velocity.getY() - GRAVITY * tickPerCalculation);

        leftRotation[3] += rotationSpeed * tickPerCalculation;
        x += velocity.getX() * tickPerCalculation;
        y += velocity.getY() * tickPerCalculation;
        z += velocity.getZ() * tickPerCalculation;

        for (int i = 0; i < 8; i++) {
            double[] vertex = VERTEXES.get(i);
            double[] rotatedVertex = RotateUtil.rotateDotByVector(vertex, new double[]{rightRotation[0], rightRotation[1], rightRotation[2]}, rightRotation[3]);
            double[] rotatedVertex2 = RotateUtil.rotateDotByVector(rotatedVertex, new double[]{leftRotation[0], leftRotation[1], leftRotation[2]}, leftRotation[3]);
            double[] vertexPos = new double[]{rotatedVertex2[0] + x, rotatedVertex2[1] + y, rotatedVertex2[2] + z};

            Block block = world.getBlockAt(new Location(world, vertexPos[0], vertexPos[1], vertexPos[2]));
            if (block.getBlockData().getMaterial() == Material.AIR) {
                continue;
            }
            Location blockLocation = block.getLocation();
            Vector collisionVector = new Vector(x, y, z).subtract(blockLocation.toVector());


            if (Math.abs(collisionVector.getX()) < 0.5) {
                if (Math.abs(collisionVector.getY()) < 0.5) {
                    velocity.setZ(velocity.getZ() * -0.8);
                } else {
                    velocity.setY(velocity.getY() * -0.8);
                }
            } else {
                velocity.setX(velocity.getX() * -0.8);
            }

        }

        object.teleport(new Location(world, x, y, z));

    }
}
