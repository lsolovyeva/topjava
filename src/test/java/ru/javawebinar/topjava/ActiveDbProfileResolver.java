package ru.javawebinar.topjava;

import org.springframework.lang.NonNull;
import org.springframework.test.context.support.DefaultActiveProfilesResolver;

import java.util.Arrays;

import static ru.javawebinar.topjava.Profiles.*;

//http://stackoverflow.com/questions/23871255/spring-profiles-simple-example-of-activeprofilesresolver
public class ActiveDbProfileResolver extends DefaultActiveProfilesResolver {
    @Override
    public @NonNull
    String[] resolve(@NonNull Class<?> aClass) {
        // https://stackoverflow.com/a/52438829/548473
        String[] activeProfiles = super.resolve(aClass);
        String[] activeProfilesWithDb = Arrays.copyOf(activeProfiles, activeProfiles.length + 2);
        activeProfilesWithDb[activeProfiles.length] = Profiles.getActiveDbProfile();
        activeProfilesWithDb[activeProfiles.length + 1] = Profiles.getActiveRepositoryProfile(REPOSITORY_IMPLEMENTATION);
        return activeProfilesWithDb;
    }
}
