package com.medshare.dto;

import java.math.BigDecimal;

public class ImpactMetrics {
    private Long totalMedicinesListed;
    private Long totalMedicinesDonated;
    private Long totalMedicinesAvailable;
    private Double totalValueSaved;
    private Long totalPeopleHelped;
    private Long totalDonors;
    private Long totalReceivers;

    // Constructors
    public ImpactMetrics() {}

    public ImpactMetrics(Long totalMedicinesListed, Long totalMedicinesDonated, Long totalMedicinesAvailable,
                        Double totalValueSaved, Long totalPeopleHelped, Long totalDonors, Long totalReceivers) {
        this.totalMedicinesListed = totalMedicinesListed;
        this.totalMedicinesDonated = totalMedicinesDonated;
        this.totalMedicinesAvailable = totalMedicinesAvailable;
        this.totalValueSaved = totalValueSaved;
        this.totalPeopleHelped = totalPeopleHelped;
        this.totalDonors = totalDonors;
        this.totalReceivers = totalReceivers;
    }

    // Getters and Setters
    public Long getTotalMedicinesListed() {
        return totalMedicinesListed;
    }

    public void setTotalMedicinesListed(Long totalMedicinesListed) {
        this.totalMedicinesListed = totalMedicinesListed;
    }

    public Long getTotalMedicinesDonated() {
        return totalMedicinesDonated;
    }

    public void setTotalMedicinesDonated(Long totalMedicinesDonated) {
        this.totalMedicinesDonated = totalMedicinesDonated;
    }

    public Long getTotalMedicinesAvailable() {
        return totalMedicinesAvailable;
    }

    public void setTotalMedicinesAvailable(Long totalMedicinesAvailable) {
        this.totalMedicinesAvailable = totalMedicinesAvailable;
    }

    public Double getTotalValueSaved() {
        return totalValueSaved;
    }

    public void setTotalValueSaved(Double totalValueSaved) {
        this.totalValueSaved = totalValueSaved;
    }

    public Long getTotalPeopleHelped() {
        return totalPeopleHelped;
    }

    public void setTotalPeopleHelped(Long totalPeopleHelped) {
        this.totalPeopleHelped = totalPeopleHelped;
    }

    public Long getTotalDonors() {
        return totalDonors;
    }

    public void setTotalDonors(Long totalDonors) {
        this.totalDonors = totalDonors;
    }

    public Long getTotalReceivers() {
        return totalReceivers;
    }

    public void setTotalReceivers(Long totalReceivers) {
        this.totalReceivers = totalReceivers;
    }
}