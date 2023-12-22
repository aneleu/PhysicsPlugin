package me.aneleu.physicsplugin;

import org.apache.commons.math3.complex.Quaternion;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RotateUtil {

    public static double @NotNull [] rotateDotByVector(double[] point,double[] axis, double theta) {

        double u = axis[0];
        double v = axis[1];
        double w = axis[2];

        double magnitude = Math.sqrt(u * u + v * v + w * w);
        if (magnitude != 0) {
            u /= magnitude;
            v /= magnitude;
            w /= magnitude;
        } else {
            w = 1;
        }

        double x = point[0];
        double y = point[1];
        double z = point[2];

        double[] result = new double[3];
        double v1 = u * x + v * y + w * z;
        result[0] = (u * v1 * (1 - Math.cos(theta)) + x * Math.cos(theta) + (-w * y + v * z) * Math.sin(theta));
        result[1] = (v * v1 * (1 - Math.cos(theta)) + y * Math.cos(theta) + (w * x - u * z) * Math.sin(theta));
        result[2] = (w * v1 * (1 - Math.cos(theta)) + z * Math.cos(theta) + (-v * x + u * y) * Math.sin(theta));

        return result;
    }

    @Contract("_, _ -> new")
    public static double @NotNull [] axisangleToQuaternion(double[] axis, double angle) {
        double norm = Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2]);
        double x = axis[0] / norm;
        double y = axis[1] / norm;
        double z = axis[2] / norm;

        double w = Math.cos(angle / 2);
        double qx = x * Math.sin(angle / 2);
        double qy = y * Math.sin(angle / 2);
        double qz = z * Math.sin(angle / 2);

        return new double[]{w, qx, qy, qz};
    }

    @Contract("_ -> new")
    public static double @NotNull [] quaternionToAxisangle(double[] quaternion) {
        double w = quaternion[0];
        double qx = quaternion[1];
        double qy = quaternion[2];
        double qz = quaternion[3];

        double angle = 2 * Math.acos(w);

        double norm = Math.sqrt(1 - w * w);
        double x = qx / norm;
        double y = qy / norm;
        double z = qz / norm;

        return new double[]{x, y, z, angle};
    }

    public static double @NotNull [] combineAxisAngles(double[] axis1, double angle1, double[] axis2, double angle2) {
        double[] quaternion1 = axisangleToQuaternion(axis1, angle1);
        double[] quaternion2 = axisangleToQuaternion(axis2, angle2);
        Quaternion q1 = new Quaternion(quaternion1[0], quaternion1[1], quaternion1[2], quaternion1[3]);
        Quaternion q2 = new Quaternion(quaternion2[0], quaternion2[1], quaternion2[2], quaternion2[3]);

        Quaternion combined = q1.multiply(q2);

        return quaternionToAxisangle(new double[]{combined.getQ0(), combined.getQ1(), combined.getQ2(), combined.getQ3()});
    }

}
