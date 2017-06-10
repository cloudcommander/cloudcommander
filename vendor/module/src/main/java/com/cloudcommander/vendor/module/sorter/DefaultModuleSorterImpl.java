package com.cloudcommander.vendor.module.sorter;

import com.cloudcommander.vendor.module.modules.Module;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.*;

/**
 * Created by Adrian Tello on 09/06/2017.
 */
public class DefaultModuleSorterImpl implements ModuleSorter{

    @Override
    public List<Module> sort(final Collection<Module> modules) {
        List<Module> sortedModules = new ArrayList<>(modules.size());

        Map<String, Module> modulesMap = createMap(modules);
        Map<String, Set<String>> modulesDepsMap = createDependenciesMap(modulesMap);

        while(!modulesDepsMap.isEmpty()){
            Optional<String> moduleNameWoDepsOptional = getModuleNameWoDeps(modulesDepsMap);

            if(moduleNameWoDepsOptional.isPresent()){
                String moduleNameWoDeps = moduleNameWoDepsOptional.get();
                removeModuleNameFromMap(moduleNameWoDeps, modulesDepsMap);

                sortedModules.add(modulesMap.get(moduleNameWoDeps));
            }else{
                throw new IllegalArgumentException("Module dependency three could't be resolved. Are all dependencies present? Is there some circular dependency?");
            }
        }

        return ImmutableList.copyOf(sortedModules);
    }

    private Map<String, Module> createMap(final Collection<Module> modules){
        Map<String, Module> modulesMap = new HashMap<>(modules.size());

        for(Module module: modules){
            String moduleName = module.getName();

            if(modulesMap.containsKey(moduleName)){
                throw new IllegalArgumentException("There is already a module named: " + moduleName);
            }else{
                modulesMap.put(moduleName, module);
            }
        }

        return modulesMap;
    }

    private Optional<String> getModuleNameWoDeps(final Map<String, Set<String>> modulesMap) {
        String moduleName = null;

        for(Map.Entry<String, Set<String>> moduleMapEntry: modulesMap.entrySet()){
            Set<String> dependencies = moduleMapEntry.getValue();
            if(dependencies.isEmpty()){
                moduleName = moduleMapEntry.getKey();
                break;
            }
        }

        return Optional.ofNullable(moduleName);
    }

    private void removeModuleNameFromMap(final String moduleName, final Map<String, Set<String>> modulesMap) {
        modulesMap.remove(moduleName);

        for(Map.Entry<String, Set<String>> modulesMapEntry: modulesMap.entrySet()){
            Set<String> dependencies = modulesMapEntry.getValue();
            dependencies.remove(moduleName);
        }
    }

    private Map<String,Set<String>> createDependenciesMap(Map<String, Module> modulesMap) {
        Map<String,Set<String>> dependenciesMap = new HashMap<>(modulesMap.size());

        for(Map.Entry<String, Module> modulesMapEntry: modulesMap.entrySet()){
            String moduleName = modulesMapEntry.getKey();
            Module module = modulesMapEntry.getValue();
            Collection<String> dependencies = module.getRequiredModuleNames();

            dependenciesMap.put(moduleName, new HashSet<>(dependencies));
        }

        return dependenciesMap;
    }
}
