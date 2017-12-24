package com.yyktools.thrustcalc2;

public class ThrustCalculator {
    private double load, hp, thrust, speed;

    public ThrustCalculator() {
    }

    public void calculate(double diam, double pitch, double rpm, int nBlades) {
        if (nBlades < 2 || nBlades > 3)
            throw new RuntimeException("wrong number of blades");
        double H = -0.05 * nBlades * nBlades + 0.65 * nBlades - 0.1;
        double I = pitch / diam;
        double J = diam * diam * diam * diam * pitch;  // load =POWER(D36,4)*E36
        load = J;
        double K = pitch * 0.000947 * rpm; // speed (mph) =E36*0.000947*F36
        speed = K;
        // hp =(POWER(F36,3)*POWER(D36,5))/1000000000000000000*I36*7.143*G36/2
        double L = Math.pow(rpm, 3) * Math.pow(diam, 5) / 1000000000000000000.0 * I * 7.143 * nBlades / 2;
        hp = L;
        // thrust(lds)  =(POWER(F36,2)*POWER(D36,4))/1000000000000*2.83*H36
        double M = (Math.pow(rpm, 2) * Math.pow(diam, 4) / 1000000000000.0 * 2.83 * H);
        thrust = M;
    }

    public double getLoad() {
        return load;
    }

    public double getHp() {
        return hp;
    }

    public double getThrust() {
        return thrust;
    }

    public double getSpeed() {
        return speed;
    }
}

// end of file
