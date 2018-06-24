package com.paypal.android.sdk;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class fa {
    private static final String f379a = fa.class.getSimpleName();
    private static final Map f380b;
    private static final Set f381c;

    static {
        Map hashMap = new HashMap();
        f380b = hashMap;
        hashMap.put("c14", "erpg");
        f380b.put("c25", "page");
        f380b.put("c26", "link");
        f380b.put("c27", "pgln");
        f380b.put("c29", "eccd");
        f380b.put("c35", "lgin");
        f380b.put("vers", "vers");
        f380b.put("c50", "rsta");
        f380b.put("gn", "pgrp");
        f380b.put("v49", "mapv");
        f380b.put("v51", "mcar");
        f380b.put("v52", "mosv");
        f380b.put("v53", "mdvs");
        f380b.put("clid", "clid");
        f380b.put("apid", "apid");
        f380b.put("calc", "calc");
        f380b.put("e", "e");
        f380b.put("t", "t");
        f380b.put("g", "g");
        f380b.put("srce", "srce");
        f380b.put("vid", "vid");
        f380b.put("bchn", "bchn");
        f380b.put("adte", "adte");
        f380b.put("sv", "sv");
        f380b.put("dsid", "dsid");
        f380b.put("bzsr", "bzsr");
        f380b.put("prid", "prid");
        Set hashSet = new HashSet();
        f381c = hashSet;
        hashSet.add("v25");
        f381c.add("v31");
        f381c.add("c37");
    }

    public static bu m364a(bu buVar) {
        Map map = buVar.f196b;
        Map hashMap = new HashMap();
        for (CharSequence charSequence : map.keySet()) {
            if (!C0441d.m267c(charSequence)) {
                if (f381c.contains(charSequence)) {
                    new StringBuilder("SC key ").append(charSequence).append(" not used in FPTI, skipping");
                } else if (f380b.containsKey(charSequence)) {
                    String str = (String) f380b.get(charSequence);
                    if (str != null) {
                        hashMap.put(str, map.get(charSequence));
                    }
                } else {
                    new StringBuilder("No mapping for SC key ").append(charSequence).append(", skipping");
                }
            }
        }
        return new bu(buVar.f195a, hashMap);
    }
}
