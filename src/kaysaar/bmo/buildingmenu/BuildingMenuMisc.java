package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.misc.AshMisc;
import ashlib.data.plugins.reflection.ReflectionBetterUtilis;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.econ.CommodityOnMarketAPI;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.MutableCommodityQuantity;
import com.fs.starfarer.api.campaign.listeners.ListenerUtil;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.impl.campaign.econ.impl.BaseIndustry;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.ids.Strings;
import com.fs.starfarer.api.loading.IndustrySpecAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;

import java.awt.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;
import java.util.*;

public class BuildingMenuMisc {
    private static class ReflectionUtilis {
        // Code taken and modified from Grand Colonies
        private static final Class<?> fieldClass;
        private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
        private static final MethodHandle setFieldHandle;
        private static final MethodHandle getFieldHandle;
        private static final MethodHandle getFieldNameHandle;
        private static final MethodHandle setFieldAccessibleHandle;
        private static final Class<?> methodClass;
        private static final MethodHandle getMethodNameHandle;
        private static final MethodHandle invokeMethodHandle;
        private static final MethodHandle setMethodAccessable;

        static {
            try {
                fieldClass = Class.forName("java.lang.reflect.Field", false, Class.class.getClassLoader());
                setFieldHandle = lookup.findVirtual(fieldClass, "set", MethodType.methodType(Void.TYPE, Object.class, Object.class));
                getFieldHandle = lookup.findVirtual(fieldClass, "get", MethodType.methodType(Object.class, Object.class));
                getFieldNameHandle = lookup.findVirtual(fieldClass, "getName", MethodType.methodType(String.class));
                setFieldAccessibleHandle = lookup.findVirtual(fieldClass, "setAccessible", MethodType.methodType(Void.TYPE, boolean.class));

                methodClass = Class.forName("java.lang.reflect.Method", false, Class.class.getClassLoader());
                getMethodNameHandle = lookup.findVirtual(methodClass, "getName", MethodType.methodType(String.class));
                invokeMethodHandle = lookup.findVirtual(methodClass, "invoke", MethodType.methodType(Object.class, Object.class, Object[].class));
                setMethodAccessable = lookup.findVirtual(methodClass, "setAccessible", MethodType.methodType(Void.TYPE, boolean.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public static Object getPrivateVariable(String fieldName, Object instanceToGetFrom) {
            try {
                Class<?> instances = instanceToGetFrom.getClass();
                while (instances != null) {
                    for (Object obj : instances.getDeclaredFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            return getFieldHandle.invoke(obj, instanceToGetFrom);
                        }
                    }
                    for (Object obj : instances.getFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            return getFieldHandle.invoke(obj, instanceToGetFrom);
                        }
                    }
                    instances = instances.getSuperclass();
                }
                return null;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public static Object getPrivateVariableFromSuperClass(String fieldName, Object instanceToGetFrom) {
            try {
                Class<?> instances = instanceToGetFrom.getClass();
                while (instances != null) {
                    for (Object obj : instances.getDeclaredFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            return getFieldHandle.invoke(obj, instanceToGetFrom);
                        }
                    }
                    for (Object obj : instances.getFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            return getFieldHandle.invoke(obj, instanceToGetFrom);
                        }
                    }
                    instances = instances.getSuperclass();
                }
                return null;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public static void setPrivateVariableFromSuperclass(String fieldName, Object instanceToModify, Object newValue) {
            try {
                Class<?> instances = instanceToModify.getClass();
                while (instances != null) {
                    for (Object  obj : instances.getDeclaredFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            setFieldHandle.invoke(obj, instanceToModify, newValue);
                            return;
                        }
                    }
                    for (Object obj : instances.getFields()) {
                        setFieldAccessibleHandle.invoke(obj, true);
                        String name = (String) getFieldNameHandle.invoke(obj);
                        if (name.equals(fieldName)) {
                            setFieldHandle.invoke(obj, instanceToModify, newValue);
                            return;
                        }
                    }
                    instances = instances.getSuperclass();
                }
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public static boolean hasMethodOfName(String name, Object instance) {
            try {
                for (Object method : instance.getClass().getMethods()) {
                    if (getMethodNameHandle.invoke(method).equals(name)) {
                        return true;
                    }
                }
                return false;
            } catch (Throwable e) {
                return false;
            }
        }

        public static Object invokeMethod(String methodName, Object instance, Object... arguments) {
            try {
                Object method = instance.getClass().getMethod(methodName);
                return invokeMethodHandle.invoke(method, instance, arguments);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public static List<UIComponentAPI> getChildrenCopy(UIPanelAPI panel) {
            try {
                return (List<UIComponentAPI>) invokeMethod("getChildrenCopy", panel);
            } catch (Throwable e) {
                return null;
            }
        }
        public static Pair<Object, Class<?>[]> getMethodFromSuperclass(String methodName, Object instance) {
            Class<?> currentClass = instance.getClass();

            while (currentClass != null) {
                // Retrieve all declared methods in the current class
               Object[] methods = currentClass.getDeclaredMethods();

                for (Object method : methods) {
                    try {
                        // Retrieve the MethodHandle for the getParameterTypes method
                        MethodHandle getParameterTypesHandle = ReflectionBetterUtilis.getParameterTypesHandle(method.getClass(), "getParameterTypes");
                        // Use the MethodHandle to retrieve the method's name

                        // Check if the method name matches
                        if (getMethodNameHandle.invoke(method).equals(methodName)) {
                            // Invoke the MethodHandle to get the parameter types
                            Class<?>[] parameterTypes = (Class<?>[]) getParameterTypesHandle.invoke(method);
                            return new Pair<>(method, parameterTypes);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();  // Handle any reflection errors
                    }
                }
                // Move to the superclass if no match is found
                currentClass = currentClass.getSuperclass();
            }

            // Return null if the method was not found in the class hierarchy
            return null;
        }
        public static Object invokeMethodWithAutoProjection(String methodName, Object instance, Object... arguments) {
            // Retrieve the method and its parameter types
            Pair<Object, Class<?>[]> methodPair = getMethodFromSuperclass(methodName, instance);

            // Check if the method was found
            if (methodPair == null) {
                try {
                    throw new NoSuchMethodException("Method " + methodName + " not found in class hierarchy of " + instance.getClass().getName());
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            Object method = methodPair.one;
            Class<?>[] parameterTypes = methodPair.two;

            // Prepare arguments by projecting them to the correct types
            Object[] projectedArgs = new Object[parameterTypes.length];
            for (int index = 0; index < parameterTypes.length; index++) {
                Object arg = (arguments.length > index) ? arguments[index] : null;

                if (arg == null) {
                    // If the expected type is a primitive type, throw an exception
                    if (parameterTypes[index].isPrimitive()) {
                        throw new IllegalArgumentException("Argument at index " + index + " cannot be null for primitive type " + parameterTypes[index].getName());
                    }
                    projectedArgs[index] = null; // Keep nulls as null for reference types
                } else {
                    // Try to convert the argument to the expected parameter type
                    try {
                        projectedArgs[index] = convertArgument(arg, parameterTypes[index]);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Cannot convert argument at index " + index + " to " + parameterTypes[index].getName(), e);
                    }
                }
            }

            // Ensure the method is accessible
            try {
                setMethodAccessable.invoke(method, true);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            // Invoke the method with the projected arguments
            try {
                return invokeMethodHandle.invoke(method, instance, projectedArgs);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        // Helper function to convert an argument to the expected type
        public static Object convertArgument(Object arg, Class<?> targetType) {
            if (targetType.isAssignableFrom(arg.getClass())) {
                return arg; // Use as-is if types match
            } else if (targetType.isPrimitive()) {
                // Handle primitive types by boxing
                if (targetType == int.class) {
                    return ((Number) arg).intValue();
                } else if (targetType == long.class) {
                    return ((Number) arg).longValue();
                } else if (targetType == double.class) {
                    return ((Number) arg).doubleValue();
                } else if (targetType == float.class) {
                    return ((Number) arg).floatValue();
                } else if (targetType == short.class) {
                    return ((Number) arg).shortValue();
                } else if (targetType == byte.class) {
                    return ((Number) arg).byteValue();
                } else if (targetType == boolean.class) {
                    return arg;
                } else if (targetType == char.class) {
                    return arg;
                } else {
                    throw new IllegalArgumentException("Unsupported primitive type: " + targetType.getName());
                }
            } else {
                // For reference types, perform a cast if possible
                return targetType.cast(arg);
            }
        }
    }
    private static class ProductionUtil{
        public static UIPanelAPI getCoreUI() {
            CampaignUIAPI campaignUI;
            campaignUI = Global.getSector().getCampaignUI();
            InteractionDialogAPI dialog = campaignUI.getCurrentInteractionDialog();

            CoreUIAPI core;
            if (dialog == null) {
                core = (CoreUIAPI) ReflectionUtilis.invokeMethod("getCore",campaignUI);
            }
            else {
                core = (CoreUIAPI) ReflectionUtilis.invokeMethod( "getCoreUI",dialog);
            }
            return core == null ? null : (UIPanelAPI) core;
        }

        public static UIPanelAPI getCurrentTab() {
            UIPanelAPI coreUltimate = getCoreUI();
            UIPanelAPI core = (UIPanelAPI) ReflectionUtilis.invokeMethod("getCurrentTab",coreUltimate);
            return core == null ? null : (UIPanelAPI) core;
        }
    }
    public static ArrayList<IndustrySpecAPI> getSpecsOfParent(String parentTag) {
        ArrayList<IndustrySpecAPI> specs = new ArrayList<>();
        if(parentTag==null)return specs;
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if (allIndustrySpec.hasTag(parentTag) && allIndustrySpec.hasTag("sub_item")) {
                specs.add(allIndustrySpec);
            }
        }
        return specs;
    }

    public static LinkedHashMap<IndustrySpecAPI, ArrayList<IndustrySpecAPI>> getSpecMapParentChild() {
        LinkedHashMap<IndustrySpecAPI, ArrayList<IndustrySpecAPI>> map = new LinkedHashMap<>();
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if (allIndustrySpec.hasTag("parent_item")) {
                map.put(allIndustrySpec, getSpecsOfParent(allIndustrySpec.getData()));
            }
        }
        return map;
    }

    public static Set<IndustrySpecAPI> getIndustryTree(String progenitor) {
        Set<IndustrySpecAPI> specs = new LinkedHashSet<>();
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if (allIndustrySpec.getDowngrade() == null) continue;
            IndustrySpecAPI currentOne = allIndustrySpec;
            Set<IndustrySpecAPI> specsToProgenitor = new LinkedHashSet<>();
            while (currentOne.getDowngrade() != null) {
                specsToProgenitor.add(currentOne);
                if(currentOne.getDowngrade().equals(currentOne.getId()))break; // KOL, WHY
                currentOne = Global.getSettings().getIndustrySpec(currentOne.getDowngrade());
            }
            if (currentOne.getId().equals(progenitor)) {
                specs.addAll(specsToProgenitor);
            }
        }
        return specs;
    }
    public static void createTooltipForIndustry(
            BaseIndustry baseIndustry, Industry.IndustryTooltipMode mode, TooltipMakerAPI tooltip, boolean expanded,boolean hasTitle,float width,boolean showMods) {
        float pad = 3f;
        float opad = 10f;
        FactionAPI faction = baseIndustry.getMarket().getFaction();
        Color color = faction.getBaseUIColor();
        Color dark = faction.getDarkUIColor();
        Color grid = faction.getGridUIColor();
        Color bright = faction.getBrightUIColor();

        Color gray = Misc.getGrayColor();
        Color highlight = Misc.getHighlightColor();
        Color bad = Misc.getNegativeHighlightColor();

        MarketAPI copy = baseIndustry.getMarket().clone();
        copy.setSuppressedConditions(baseIndustry.getMarket().getSuppressedConditions());
        copy.setRetainSuppressedConditionsSetWhenEmpty(true);
        baseIndustry.getMarket().setRetainSuppressedConditionsSetWhenEmpty(true);
        ReflectionUtilis.setPrivateVariableFromSuperclass("currTooltipMode",baseIndustry,mode);

        MarketAPI orig = baseIndustry.getMarket();

        ReflectionUtilis.setPrivateVariableFromSuperclass("market",baseIndustry,copy);

        boolean needToAddIndustry = !baseIndustry.getMarket().hasIndustry(baseIndustry.getId());
        if (needToAddIndustry) baseIndustry.getMarket().getIndustries().add(baseIndustry);

        if (mode != Industry.IndustryTooltipMode.NORMAL) {
            baseIndustry.getMarket().clearCommodities();
            for (CommodityOnMarketAPI curr : baseIndustry.getMarket().getAllCommodities()) {
                curr.getAvailableStat().setBaseValue(100);
            }
        }

        baseIndustry.getMarket().reapplyConditions();
        baseIndustry.reapply();

        String type = "";
        if (baseIndustry.isIndustry()) type = " - Industry";
        if (baseIndustry.isStructure()) type = " - Structure";
        if(hasTitle){
            tooltip.addTitle(baseIndustry.getCurrentName() + type, color);
        }
        if(showMods){
            String mod = IndustryTagManager.getModNameForInd(baseIndustry.getSpec().getId());

            if(!mod.equalsIgnoreCase("vanilla")&&!baseIndustry.getSpec().hasTag("parent_item")){
                tooltip.addSectionHeading("Mod",Alignment.MID,opad);
                tooltip.addPara("This industry is from %s",opad,Color.ORANGE, mod);
                tooltip.addSectionHeading("",Alignment.MID,opad);
            }
        }

        String desc = baseIndustry.getSpec().getDesc();
        String override = (String) ReflectionUtilis.invokeMethodWithAutoProjection("getDescriptionOverride",baseIndustry);
        if (override != null) {
            desc = override;
        }
        desc = Global.getSector().getRules().performTokenReplacement(null, desc, baseIndustry.getMarket().getPrimaryEntity(), null);

        tooltip.addPara(desc, opad);

        if (baseIndustry.isIndustry() && (mode == Industry.IndustryTooltipMode.ADD_INDUSTRY ||
                mode == Industry.IndustryTooltipMode.UPGRADE || mode == Industry.IndustryTooltipMode.DOWNGRADE)) {

            int num = Misc.getNumIndustries(baseIndustry.getMarket());
            int max = Misc.getMaxIndustries(baseIndustry.getMarket());

            if (baseIndustry.isIndustry()) {
                if (mode == Industry.IndustryTooltipMode.UPGRADE) {
                    for (Industry curr : baseIndustry.getMarket().getIndustries()) {
                        if (baseIndustry.getSpec().getId().equals(curr.getSpec().getUpgrade())) {
                            if (curr.isIndustry()) {
                                num--;
                            }
                            break;
                        }
                    }
                } else if (mode == Industry.IndustryTooltipMode.DOWNGRADE) {
                    for (Industry curr : baseIndustry.getMarket().getIndustries()) {
                        if (baseIndustry.getSpec().getId().equals(curr.getSpec().getDowngrade())) {
                            if (curr.isIndustry()) {
                                num--;
                            }
                            break;
                        }
                    }
                }
            }

            Color h1 = highlight;
            if (num > max) {
                h1 = bad;
                tooltip.addPara("Maximum number of industries reached", bad, opad);
            }
        }
        ReflectionUtilis.invokeMethodWithAutoProjection("addRightAfterDescriptionSection",baseIndustry,tooltip,mode);

        if (baseIndustry.isDisrupted()) {
            int left = (int) baseIndustry.getDisruptedDays();
            if (left < 1) left = 1;
            String days = (left == 1) ? "day" : "days";

            tooltip.addPara("Operations disrupted! %s " + days + " until return to normal function.",
                    opad, Misc.getNegativeHighlightColor(), highlight, "" + left);
        }

        if (mode == Industry.IndustryTooltipMode.QUEUED) {
            tooltip.addPara("Click to remove or adjust position in queue", Misc.getPositiveHighlightColor(), opad);
            tooltip.addPara("Currently queued for construction. Does not have any impact on the colony.", opad);
            int left = (int) (baseIndustry.getSpec().getBuildTime());
            if (left < 1) left = 1;
            String days = (left == 1) ? "day" : "days";
            tooltip.addPara("Requires %s " + days + " to build.", opad, highlight, "" + left);
        } else if (!baseIndustry.isFunctional() && mode == Industry.IndustryTooltipMode.NORMAL && !baseIndustry.isDisrupted()) {
            tooltip.addPara("Currently under construction and not producing anything or providing other benefits.", opad);
            int left = (int) (baseIndustry.getBuildTime() - baseIndustry.getBuildProgress());
            if (left < 1) left = 1;
            String days = (left == 1) ? "day" : "days";
            tooltip.addPara("Requires %s more " + days + " to finish building.", opad, highlight, "" + left);
        }

        if (!baseIndustry.isAvailableToBuild() &&
                (mode == Industry.IndustryTooltipMode.ADD_INDUSTRY ||
                        mode == Industry.IndustryTooltipMode.UPGRADE ||
                        mode == Industry.IndustryTooltipMode.DOWNGRADE)) {
            String reason = baseIndustry.getUnavailableReason();
            if (reason != null) {
                tooltip.addPara(reason, bad, opad);
            }
        }
        boolean category = baseIndustry.getSpec().hasTag(Industries.TAG_PARENT);
        if (!category) {
            int credits = (int) Global.getSector().getPlayerFleet().getCargo().getCredits().get();
            String creditsStr = Misc.getDGSCredits(credits);
            if (mode == Industry.IndustryTooltipMode.UPGRADE || mode == Industry.IndustryTooltipMode.ADD_INDUSTRY) {
                int cost = (int) baseIndustry.getBuildCost();
                String costStr = Misc.getDGSCredits(cost);

                int days = (int) baseIndustry.getBuildTime();
                String daysStr = "days";
                if (days == 1) daysStr = "day";

                LabelAPI label = null;
                if (mode == Industry.IndustryTooltipMode.UPGRADE) {
                    label = tooltip.addPara("%s and %s " + daysStr + " to upgrade. You have %s.", opad,
                            highlight, costStr, "" + days, creditsStr);
                } else {
                    label = tooltip.addPara("%s and %s " + daysStr + " to build. You have %s.", opad,
                            highlight, costStr, "" + days, creditsStr);
                }
                label.setHighlight(costStr, "" + days, creditsStr);
                if (credits >= cost) {
                    label.setHighlightColors(highlight, highlight, highlight);
                } else {
                    label.setHighlightColors(bad, highlight, highlight);
                }
            } else if (mode == Industry.IndustryTooltipMode.DOWNGRADE) {
                if (baseIndustry.getSpec().getUpgrade() != null) {
                    float refundFraction = Global.getSettings().getFloat("industryRefundFraction");

                    //int cost = (int) (getBuildCost() * refundFraction);
                    IndustrySpecAPI spec = Global.getSettings().getIndustrySpec(baseIndustry.getSpec().getUpgrade());
                    int cost = (int) (spec.getCost() * refundFraction);
                    String refundStr = Misc.getDGSCredits(cost);

                    tooltip.addPara("%s refunded for downgrade.", opad, highlight, refundStr);
                }
            }
            ReflectionUtilis.invokeMethodWithAutoProjection("addPostDescriptionSection",baseIndustry,tooltip,mode);
            if (!baseIndustry.getIncome().isUnmodified()) {
                int income = baseIndustry.getIncome().getModifiedInt();
                tooltip.addPara("Monthly income: %s", opad, highlight, Misc.getDGSCredits(income));
                tooltip.addStatModGrid(300, 65, 10, pad, baseIndustry.getIncome(), true, new TooltipMakerAPI.StatModValueGetter() {
                    public String getPercentValue(MutableStat.StatMod mod) {return null;}
                    public String getMultValue(MutableStat.StatMod mod) {return null;}
                    public Color getModColor(MutableStat.StatMod mod) {return null;}
                    public String getFlatValue(MutableStat.StatMod mod) {
                        return Misc.getWithDGS(mod.value) + Strings.C;
                    }
                });
            }

            if (!baseIndustry.getUpkeep().isUnmodified()) {
                int upkeep = baseIndustry.getUpkeep().getModifiedInt();
                tooltip.addPara("Monthly upkeep: %s", opad, highlight, Misc.getDGSCredits(upkeep));
                tooltip.addStatModGrid(300, 65, 10, pad, baseIndustry.getUpkeep(), true, new TooltipMakerAPI.StatModValueGetter() {
                    public String getPercentValue(MutableStat.StatMod mod) {return null;}
                    public String getMultValue(MutableStat.StatMod mod) {return null;}
                    public Color getModColor(MutableStat.StatMod mod) {return null;}
                    public String getFlatValue(MutableStat.StatMod mod) {
                        return Misc.getWithDGS(mod.value) + Strings.C;
                    }
                });
            }
            ReflectionUtilis.invokeMethodWithAutoProjection("addPostUpkeepSection",baseIndustry,tooltip,mode);
            boolean hasSupply = false;
            Map<String, MutableCommodityQuantity> supply = (Map<String, MutableCommodityQuantity>) ReflectionUtilis.getPrivateVariableFromSuperClass("supply",baseIndustry);
            Map<String, MutableCommodityQuantity> demand =(Map<String, MutableCommodityQuantity>) ReflectionUtilis.getPrivateVariableFromSuperClass("demand",baseIndustry);
            for (MutableCommodityQuantity curr : supply.values()) {
                int qty = curr.getQuantity().getModifiedInt();
                if (qty <= 0) continue;
                hasSupply = true;
                break;
            }
            boolean hasDemand = false;
            for (MutableCommodityQuantity curr : demand.values()) {
                int qty = curr.getQuantity().getModifiedInt();
                if (qty <= 0) continue;
                hasDemand = true;
                break;
            }

            float maxIconsPerRow = 10f;
            if (hasSupply) {
                tooltip.addSectionHeading("Production", color, dark, Alignment.MID, opad);
                tooltip.beginIconGroup();
                tooltip.setIconSpacingMedium();
                float icons = 0;
                for (MutableCommodityQuantity curr : supply.values()) {
                    int qty = curr.getQuantity().getModifiedInt();
                    //if (qty <= 0) continue;

                    int normal = qty;
                    if (normal > 0) {
                        tooltip.addIcons(baseIndustry.getMarket().getCommodityData(curr.getCommodityId()), normal, IconRenderMode.NORMAL);
                    }

                    int plus = 0;
                    int minus = 0;
                    for (MutableStat.StatMod mod : curr.getQuantity().getFlatMods().values()) {
                        if (mod.value > 0) {
                            plus += (int) mod.value;
                        } else if (mod.desc != null && mod.desc.contains("shortage")) {
                            minus += (int) Math.abs(mod.value);
                        }
                    }
                    minus = Math.min(minus, plus);
                    if (minus > 0 && mode == Industry.IndustryTooltipMode.NORMAL) {
                        tooltip.addIcons(baseIndustry.getMarket().getCommodityData(curr.getCommodityId()), minus, IconRenderMode.DIM_RED);
                    }
                    icons += normal + Math.max(0, minus);
                }
                int rows = (int) Math.ceil(icons / maxIconsPerRow);
                rows = 3;
                tooltip.addIconGroup(32, rows, opad);


            }
//			else if (!isFunctional() && mode == IndustryTooltipMode.NORMAL) {
//				tooltip.addPara("Currently under construction and not producing anything or providing other benefits.", opad);
//			}
            ReflectionUtilis.invokeMethodWithAutoProjection("addPostSupplySection",baseIndustry,tooltip,hasSupply,mode);


            if (hasDemand || (boolean) ReflectionUtilis.invokeMethodWithAutoProjection("hasPostDemandSection",baseIndustry,hasDemand,mode)) {
                tooltip.addSectionHeading("Demand & effects", color, dark, Alignment.MID, opad);
            }
            if (hasDemand) {
                tooltip.beginIconGroup();
                tooltip.setIconSpacingMedium();
                float icons = 0;
                for (MutableCommodityQuantity curr : demand.values()) {
                    int qty = curr.getQuantity().getModifiedInt();
                    if (qty <= 0) continue;

                    CommodityOnMarketAPI com = orig.getCommodityData(curr.getCommodityId());
                    int available = com.getAvailable();

                    int normal = Math.min(available, qty);
                    int red = Math.max(0, qty - available);

                    if (mode != Industry.IndustryTooltipMode.NORMAL) {
                        normal = qty;
                        red = 0;
                    }
                    if (normal > 0) {
                        tooltip.addIcons(com, normal, IconRenderMode.NORMAL);
                    }
                    if (red > 0) {
                        tooltip.addIcons(com, red, IconRenderMode.DIM_RED);
                    }
                    icons += normal + Math.max(0, red);
                }
                int rows = (int) Math.ceil(icons / maxIconsPerRow);
                rows = 3;
                rows = 1;
                tooltip.addIconGroup(32, rows, opad);
            }
            ReflectionUtilis.invokeMethodWithAutoProjection("addPostDemandSection",baseIndustry,tooltip,hasDemand,mode);
            if (!needToAddIndustry) {
                //addAICoreSection(tooltip, AICoreDescriptionMode.TOOLTIP);
                baseIndustry.addInstalledItemsSection(mode, tooltip, expanded);
                baseIndustry.addImprovedSection(mode, tooltip, expanded);

                ListenerUtil.addToIndustryTooltip(baseIndustry, mode, tooltip, width, expanded);
            }
            tooltip.addPara("*Shown production and demand values are already adjusted based on current market size and local conditions.", gray, opad);

        }
        if (needToAddIndustry) {
            baseIndustry.unapply();
            baseIndustry.getMarket().getIndustries().remove(baseIndustry);
        }
        ReflectionUtilis.setPrivateVariableFromSuperclass("market",baseIndustry,orig);
        baseIndustry.getMarket().setRetainSuppressedConditionsSetWhenEmpty(null);
        if (!needToAddIndustry) {
            baseIndustry.reapply();
        }

    }

    public static ArrayList<IndustrySpecAPI> getAllSpecsWithoutDowngrade() {
        ArrayList<IndustrySpecAPI> specs = new ArrayList<>();
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if (allIndustrySpec.getDowngrade() == null  && !allIndustrySpec.hasTag("sub_item")) {
                specs.add(allIndustrySpec);
            }
        }
        return specs;
    }
    public static ArrayList<IndustrySpecAPI> getAllSpecsWithoutDowngradeAndItsSubItems() {
        ArrayList<IndustrySpecAPI> specs = new ArrayList<>();
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if (allIndustrySpec.getDowngrade() == null) {
                specs.add(allIndustrySpec);
            }
        }
        return specs;
    }
    public static boolean isIndustryFromTreePresent(IndustrySpecAPI spec, MarketAPI marketToValidate) {
        if (spec.hasTag("parent_item")) {
            for (IndustrySpecAPI industrySpecAPI : getSpecsOfParent(spec.getData())) {
                IndustrySpecAPI current = industrySpecAPI;
                if (marketToValidate.hasIndustry(current.getId())) return true;
                while (current.getUpgrade() != null) {
                    current = Global.getSettings().getIndustrySpec(current.getUpgrade());
                    if (marketToValidate.hasIndustry(current.getId())) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            for (IndustrySpecAPI industrySpecAPI : getIndustryTree(spec.getId())) {
                if (marketToValidate.hasIndustry(industrySpecAPI.getId())) {
                    return true;
                }
            }
            return false;
        }
    }

    // New method to sort IndustrySpecAPI by name alphabetically
    public static void sortIndustrySpecsByName(ArrayList<IndustrySpecAPI> industrySpecs) {
        Collections.sort(industrySpecs, new Comparator<IndustrySpecAPI>() {
            @Override
            public int compare(IndustrySpecAPI spec1, IndustrySpecAPI spec2) {
                return spec1.getName().compareToIgnoreCase(spec2.getName());
            }
        });
    }
    public static void sortDropDownButtonsByName(ArrayList<DropDownButton> buttons, final boolean ascending) {
        Collections.sort(buttons, new Comparator<DropDownButton>() {
            @Override
            public int compare(DropDownButton button1,DropDownButton button2) {
                String name1 = getButtonName(button1);
                String name2 = getButtonName(button2);
                return ascending ? name1.compareToIgnoreCase(name2) : name2.compareToIgnoreCase(name1);
            }
        });
    }

    // Sort by Type
    public static void sortDropDownButtonsByType(ArrayList<DropDownButton> buttons, final boolean ascending) {
        Collections.sort(buttons, new Comparator<DropDownButton>() {
            @Override
            public int compare(DropDownButton button1, DropDownButton button2) {
                String type1 = getButtonType(button1);
                String type2 = getButtonType(button2);
                return ascending ? type1.compareToIgnoreCase(type2) : type2.compareToIgnoreCase(type1);
            }
        });
    }

    // Sort by Build Time (Days)
    public static void sortDropDownButtonsByDays(ArrayList<DropDownButton> buttons, final boolean ascending) {
        Collections.sort(buttons, new Comparator<DropDownButton>() {
            @Override
            public int compare(DropDownButton button1, DropDownButton button2) {
                float days1 = calculateBuildTime(button1);
                float days2 = calculateBuildTime(button2);
                return ascending ? Float.compare(days1, days2) : Float.compare(days2, days1);
            }
        });
    }

    // Sort by Cost
    public static void sortDropDownButtonsByCost(ArrayList<DropDownButton> buttons, final boolean ascending) {
        Collections.sort(buttons, new Comparator<DropDownButton>() {
            @Override
            public int compare(DropDownButton button1, DropDownButton button2) {
                float cost1 = calculateCost(button1);
                float cost2 = calculateCost(button2);
                return ascending ? Float.compare(cost1, cost2) : Float.compare(cost2, cost1);
            }
        });
    }

    // Utility Methods for Sorting Logic
    private static String getButtonName(DropDownButton button) {
        return button instanceof IndustryDropDownButton
                ? ((IndustryDropDownButton) button).mainSpec.getName()
                : "Unknown";
    }

    private static String getButtonType(DropDownButton button) {
        return button instanceof IndustryDropDownButton
                ? getIndustryString(((IndustryDropDownButton) button).mainSpec)
                : "Unknown";
    }

    private static float calculateBuildTime(DropDownButton button) {
        if (button.droppableMode) {
            ArrayList<IndustrySpecAPI> subSpecs = ((IndustryDropDownButton) button).subSpecs;
            if (subSpecs != null && !subSpecs.isEmpty()) {
                float totalBuildTime = 0;
                for (IndustrySpecAPI spec : subSpecs) {
                    totalBuildTime += spec.getBuildTime();
                }
                return totalBuildTime / subSpecs.size();
            }
        }
        IndustrySpecAPI mainSpec = ((IndustryDropDownButton) button).mainSpec;
        return mainSpec != null ? mainSpec.getBuildTime() : 0;
    }

    private static float calculateCost(DropDownButton button) {
        if (button.droppableMode) {
            ArrayList<IndustrySpecAPI> subSpecs = ((IndustryDropDownButton) button).subSpecs;
            if (subSpecs != null && !subSpecs.isEmpty()) {
                float totalCost = 0;
                for (IndustrySpecAPI spec : subSpecs) {
                    totalCost += getSpecCost(spec);
                }
                return totalCost / subSpecs.size();
            }
        }
        IndustrySpecAPI mainSpec = ((IndustryDropDownButton) button).mainSpec;
        return mainSpec != null ? getSpecCost(mainSpec) : 0;
    }

    private static float getSpecCost(IndustrySpecAPI spec) {
        // Replace this logic with the actual method to get the cost in your implementation.
        return spec.getCost(); // Example placeholder method
    }

    private static String getIndustryString(IndustrySpecAPI industry) {
        if (industry.hasTag("parent_item")) {
            return "Variable";
        } else if (industry.hasTag("industry")) {
            return "Industry";
        } else if (industry.hasTag("structure")) {
            return "Structure";
        }
        return "Unknown";
    }
    public static ArrayList<DropDownButton> searchIndustryByName(ArrayList<DropDownButton> buttons, String searchString, int threshold) {
        ArrayList<DropDownButton> matchingButtons = new ArrayList<>();

        for (DropDownButton button : buttons) {
            if (button instanceof IndustryDropDownButton) {
                IndustryDropDownButton industryButton = (IndustryDropDownButton) button;

                // Check mainSpec name
                if (industryButton.mainSpec != null && isValid(searchString, industryButton.mainSpec.getName(), threshold)) {
                    matchingButtons.add(button);
                    continue;
                }

                // Check subSpecs names if droppableMode
                if (button.droppableMode && industryButton.subSpecs != null) {
                    for (IndustrySpecAPI subSpec : industryButton.subSpecs) {
                        if (isValid(searchString, subSpec.getName(), threshold)) {
                            matchingButtons.add(button);
                            break; // Stop checking subSpecs for this button
                        }
                    }
                }
            }
        }

        return matchingButtons;
    }

    public static boolean isValid(String searchString, String target, int threshold) {
        String lowerSearchString = searchString.toLowerCase();
        String lowerTarget = target.toLowerCase();
        return AshMisc.levenshteinDistance(lowerSearchString, lowerTarget) <= threshold || lowerTarget.contains(lowerSearchString);
    }
    public static Set<String>getUpgradeChildren(String progenitor){
        LinkedHashSet<String> children = new LinkedHashSet<>();
        for (IndustrySpecAPI allIndustrySpec : Global.getSettings().getAllIndustrySpecs()) {
            if(AshMisc.isStringValid(allIndustrySpec.getDowngrade())&&progenitor.equals(allIndustrySpec.getDowngrade())){
                children.add(allIndustrySpec.getId());
            }
        }
        return children;
    }

}
