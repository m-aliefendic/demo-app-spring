package com.ba.demo.api.model.user;

import com.google.common.collect.Lists;

import java.util.List;

public enum Role {
    admin{
        @Override
        public List<String> getPermissions() {
            return Lists.newArrayList("USER_UPDATE");
        }
    },
    user, company,  customerService, techSupport, marketing, manager, chiefOfficer;

    public List<String> getPermissions() {
        return Lists.newArrayList();
    }
}
