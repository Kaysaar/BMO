package kaysaar.bmo.buildingmenu;

import ashlib.data.plugins.ui.models.BasePopUpDialog;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignUIAPI;
import com.fs.starfarer.api.campaign.CoreUIAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import com.fs.starfarer.api.ui.UIPanelAPI;
import com.fs.starfarer.campaign.ui.marketinfo.IndustryPickerDialog;
import com.fs.starfarer.ui.impl.StandardTooltipV2;
import kaysaar.bmo.buildingmenu.industrytags.IndustryTagManager;
import kaysaar.bmo.buildingmenu.upgradepaths.UpgradePathManager;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

public class BuildingUITracker implements EveryFrameScript {
    public static boolean didIt = false;

    private static class ReflectionUtilis {
        // Code taken and modified from Grand Colonies
        private static final Class<?> fieldClass;
        private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
        private static final java.lang.invoke.MethodHandle setFieldHandle;
        private static final java.lang.invoke.MethodHandle getFieldHandle;
        private static final java.lang.invoke.MethodHandle getFieldNameHandle;
        private static final java.lang.invoke.MethodHandle setFieldAccessibleHandle;
        private static final Class<?> methodClass;
        private static final java.lang.invoke.MethodHandle getMethodNameHandle;
        private static final java.lang.invoke.MethodHandle invokeMethodHandle;
        private static final java.lang.invoke.MethodHandle setMethodAccessable;

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
                    for (Object obj : instances.getDeclaredFields()) {
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

        public static Object invokeMethodWithAutoProjection(String methodName, Object instance, Object... arguments) {
            try {
                Object method = instance.getClass().getMethod(methodName);
                return invokeMethodHandle.invoke(method, instance, arguments);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class ProductionUtil {
        public static UIPanelAPI getCoreUI() {
            CampaignUIAPI campaignUI;
            campaignUI = Global.getSector().getCampaignUI();
            InteractionDialogAPI dialog = campaignUI.getCurrentInteractionDialog();

            CoreUIAPI core;
            if (dialog == null) {
                core = (CoreUIAPI) ReflectionUtilis.invokeMethod("getCore", campaignUI);
            } else {
                core = (CoreUIAPI) ReflectionUtilis.invokeMethod("getCoreUI", dialog);
            }
            return core == null ? null : (UIPanelAPI) core;
        }

        public static UIPanelAPI getCurrentTab() {
            UIPanelAPI coreUltimate = getCoreUI();
            UIPanelAPI core = (UIPanelAPI) ReflectionUtilis.invokeMethod("getCurrentTab", coreUltimate);
            return core == null ? null : (UIPanelAPI) core;

        }
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return true;
    }

    @Override
    public void advance(float amount) {
        if (Global.getSector().getCampaignUI().getCurrentCoreTab() == null) {
            didIt = false;
            return;
        }
        if (!didIt) {
            List<UIComponentAPI> panels = ReflectionUtilis.getChildrenCopy(ProductionUtil.getCoreUI());
            if (panels != null) {
                if (panels.get(panels.size() - 1) instanceof IndustryPickerDialog) {
                    IndustryPickerDialog dialog = (IndustryPickerDialog) panels.get(panels.size() - 1);

                    dialog.dismiss(1);
                    didIt = true;
                    UpgradePathManager.getInstance().rePopulate();
                    IndustryTagManager.loadDefaultTags();
                    IndustryTagManager.loadModdedTags();
                    BasePopUpDialog dialog2 = new MarketDialog(null, (MarketAPI) ReflectionUtilis.getPrivateVariable("market", ReflectionUtilis.invokeMethod("getDelegate", dialog)), ReflectionUtilis.invokeMethod("getOverview", ReflectionUtilis.invokeMethod("getDelegate", dialog)));
                    CustomPanelAPI panelAPI = Global.getSettings().createCustom(1100, 700, dialog2);
                    UIPanelAPI panelAPI1 = ProductionUtil.getCoreUI();
                    dialog2.init(panelAPI, panelAPI1.getPosition().getCenterX() - (panelAPI.getPosition().getWidth() / 2), panelAPI1.getPosition().getCenterY() + (panelAPI.getPosition().getHeight() / 2), true);

                }

            }

        }
    }
}
