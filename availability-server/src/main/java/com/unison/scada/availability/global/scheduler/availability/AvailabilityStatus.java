package com.unison.scada.availability.global.scheduler.availability;

public enum AvailabilityStatus{
    FULL_PERFORMANCE(10),
    PARTIAL_PERFORMANCE(20),
    TECHNICAL_STANDBY(30),
    OUT_OF_ENVIRONMENTAL(40),
    REQUESTED_SHUTDOWN(50),
    OUT_OF_ELECTRICAL_SPECIFICATION(60),
    SCHEDULED_MAINTENANCE(70),
    PLANNED_CORRECTIVE_ACTION(80),
    FORCED_OUTAGE(90),
    SUSPENDED(100),
    FORCE_MAJEURE(110),
    INFORMATION_UNAVAILABLE(120);

    private final int value;

    public int value() {
        return value;
    }

    AvailabilityStatus(int value) {
        this.value = value;
    }
}