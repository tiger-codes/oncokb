package org.mskcc.cbio.oncokb.util;

import org.apache.commons.collections.CollectionUtils;
import org.mskcc.cbio.oncokb.model.Evidence;
import org.mskcc.cbio.oncokb.model.LevelOfEvidence;

import java.util.*;

/**
 * Created by Hongxin Zhang
 */
public class LevelUtils {
    private static final List<LevelOfEvidence> PUBLIC_LEVELS = Collections.unmodifiableList(
        Arrays.asList(
            LevelOfEvidence.LEVEL_Px3, LevelOfEvidence.LEVEL_Px2, LevelOfEvidence.LEVEL_Px1,
            LevelOfEvidence.LEVEL_Dx3, LevelOfEvidence.LEVEL_Dx2, LevelOfEvidence.LEVEL_Dx1,
            LevelOfEvidence.LEVEL_R2, LevelOfEvidence.LEVEL_4, LevelOfEvidence.LEVEL_3B, LevelOfEvidence.LEVEL_3A,
            LevelOfEvidence.LEVEL_2B, LevelOfEvidence.LEVEL_2A, LevelOfEvidence.LEVEL_1, LevelOfEvidence.LEVEL_R1
            )
    );

    // Levels related to therapy

    private static final List<LevelOfEvidence> THERAPEUTIC_SENSITIVE_LEVELS = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_4, LevelOfEvidence.LEVEL_3B, LevelOfEvidence.LEVEL_3A,
            LevelOfEvidence.LEVEL_2B, LevelOfEvidence.LEVEL_2A, LevelOfEvidence.LEVEL_1)
    );

    private static final List<LevelOfEvidence> THERAPEUTIC_RESISTANCE_LEVELS = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_R2, LevelOfEvidence.LEVEL_R1)
    );


    // This is for sorting treatments when all levels are in one array. The only difference at the moment is the level 3A will be prioritised over 2B.
    // But 2B is still higher level of 3A
    private static final List<LevelOfEvidence> THERAPEUTIC_LEVELS_WITH_PRIORITY = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_R2, LevelOfEvidence.LEVEL_4, LevelOfEvidence.LEVEL_3B,
            LevelOfEvidence.LEVEL_2B, LevelOfEvidence.LEVEL_3A, LevelOfEvidence.LEVEL_2A, LevelOfEvidence.LEVEL_1, LevelOfEvidence.LEVEL_R1)
    );

    private static final List<LevelOfEvidence> THERAPEUTIC_OTHER_INDICATION_LEVELS = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_2B, LevelOfEvidence.LEVEL_3B)
    );

    // Levels related to therapy
    private static final List<LevelOfEvidence> PROGNOSTIC_LEVELS = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_Px3, LevelOfEvidence.LEVEL_Px2, LevelOfEvidence.LEVEL_Px1)
    );

    // Levels related to therapy
    private static final List<LevelOfEvidence> DIAGNOSTIC_LEVELS = Collections.unmodifiableList(
        Arrays.asList(LevelOfEvidence.LEVEL_Dx3, LevelOfEvidence.LEVEL_Dx2, LevelOfEvidence.LEVEL_Dx1)
    );

    public static Integer compareLevel(LevelOfEvidence a, LevelOfEvidence b) {
        return compareLevel(a, b, PUBLIC_LEVELS);
    }

    public static Integer compareLevel(LevelOfEvidence a, LevelOfEvidence b, List<LevelOfEvidence> levels) {
        if (!levels.contains(a)) {
            if (!levels.contains(b)) {
                return 0;
            }
            return 1;
        }
        if (!levels.contains(b)) {
            return -1;
        }

        return levels.indexOf(b) - levels.indexOf(a);
    }

    public static LevelOfEvidence getHighestLevelFromEvidence(Set<Evidence> evidences) {
        if (evidences != null) {
            Integer highestLevelIndex = -1;

            for (Evidence evidence : evidences) {
                LevelOfEvidence level = evidence.getLevelOfEvidence();
                if (level != null) {
                    Integer _index = PUBLIC_LEVELS.indexOf(level);
                    highestLevelIndex = _index > highestLevelIndex ? _index : highestLevelIndex;
                }
            }

            return highestLevelIndex > -1 ? PUBLIC_LEVELS.get(highestLevelIndex) : null;

        }
        return null;
    }

    public static LevelOfEvidence getHighestLevel(Set<LevelOfEvidence> levels) {
        return getHighestLevelByType(levels, LEVELS);
    }

    public static LevelOfEvidence getHighestSensitiveLevel(Set<LevelOfEvidence> levels) {
        return getHighestLevelByType(levels, SENSITIVE_LEVELS);
    }
    public static LevelOfEvidence getHighestResistanceLevel(Set<LevelOfEvidence> levels) {
        return getHighestLevelByType(levels, RESISTANCE_LEVELS);
    }

    public static LevelOfEvidence getHighestLevelByType(Set<LevelOfEvidence> levels, List<LevelOfEvidence> levelPool) {
        Integer highestLevelIndex = -1;
        for (LevelOfEvidence levelOfEvidence : levels) {
            if (levelOfEvidence != null) {
                Integer _index = levelPool.indexOf(levelOfEvidence);
                highestLevelIndex = _index > highestLevelIndex ? _index : highestLevelIndex;
            }
        }
        return highestLevelIndex > -1 ? levelPool.get(highestLevelIndex) : null;
    }

    public static LevelOfEvidence getHighestLevelFromEvidenceByLevels(Set<Evidence> evidences, Set<LevelOfEvidence> levels) {
        if (levels == null) {
            return getHighestLevelFromEvidence(evidences);
        }
        if (evidences != null) {
            Integer highestLevelIndex = -1;

            for (Evidence evidence : evidences) {
                LevelOfEvidence level = evidence.getLevelOfEvidence();
                if (levels.contains(level)) {
                    if (level != null) {
                        Integer _index = PUBLIC_LEVELS.indexOf(level);
                        highestLevelIndex = _index > highestLevelIndex ? _index : highestLevelIndex;
                    }
                }
            }

            return highestLevelIndex > -1 ? PUBLIC_LEVELS.get(highestLevelIndex) : null;

        }
        return null;
    }

    public static Set<LevelOfEvidence> getLevelsFromEvidence(Set<Evidence> evidences) {
        Set<LevelOfEvidence> levels = new HashSet<>();
        if (evidences != null) {
            for (Evidence evidence : evidences) {
                LevelOfEvidence level = evidence.getLevelOfEvidence();
                if (!levels.contains(level)) {
                    levels.add(level);
                }
            }

        }
        return levels;
    }

    public static Set<LevelOfEvidence> getLevelsFromEvidenceByLevels(Set<Evidence> evidences, Set<LevelOfEvidence> levels) {
        Set<LevelOfEvidence> result = new HashSet<>();
        if (evidences != null) {
            for (Evidence evidence : evidences) {
                LevelOfEvidence level = evidence.getLevelOfEvidence();
                if (levels.contains(level) && !result.contains(level)) {
                    result.add(level);
                }
            }

        }
        return result;
    }

    // This is specifically designed to change level if it is for alternative allele.
    // Change level based on indication has been changed to use propagation instead.
    public static LevelOfEvidence setToAlleleLevel(LevelOfEvidence level, Boolean sameIndication) {
        List<LevelOfEvidence> convertLevels = Arrays.asList(LevelOfEvidence.LEVEL_0, LevelOfEvidence.LEVEL_1,
            LevelOfEvidence.LEVEL_2A, LevelOfEvidence.LEVEL_2B, LevelOfEvidence.LEVEL_3A);

        if (level == null)
            return null;

        if (convertLevels.contains(level)) {
            if (!sameIndication) {
                if (level.equals(LevelOfEvidence.LEVEL_3A)) {
                    return LevelOfEvidence.LEVEL_3B;
                } else {
                    return LevelOfEvidence.LEVEL_2B;
                }
            }
        }

        if (getResistanceLevels().contains(level) && !sameIndication) {
            return null;
        }

        return level;
    }

    public static LevelOfEvidence updateOrKeepLevelByIndication(LevelOfEvidence level, String propagation, Boolean sameIndication) {
        if (level == null)
            return level;

        if (!sameIndication && propagation != null) {
            return LevelOfEvidence.getByName(propagation);
        }

        return level;
    }

    public static Set<LevelOfEvidence> parseStringLevelOfEvidences(String levelOfEvidenceStr) {
        Set<LevelOfEvidence> levelOfEvidences = new HashSet<>();
        if (levelOfEvidenceStr != null) {
            String[] levelStrs = levelOfEvidenceStr.trim().split("\\s*,\\s*");
            for (int i = 0; i < levelStrs.length; i++) {
                LevelOfEvidence level = LevelOfEvidence.getByName(levelStrs[i]);
                if (level != null) {
                    levelOfEvidences.add(level);
                }
            }
        }
        return levelOfEvidences;
    }

    public static Boolean isSensitiveLevel(LevelOfEvidence levelOfEvidence) {
        Boolean flag = false;
        if (levelOfEvidence != null && THERAPEUTIC_SENSITIVE_LEVELS.contains(levelOfEvidence)) {
            flag = true;
        }
        return flag;
    }

    public static Boolean isResistanceLevel(LevelOfEvidence levelOfEvidence) {
        Boolean flag = false;
        if (levelOfEvidence != null && THERAPEUTIC_RESISTANCE_LEVELS.contains(levelOfEvidence)) {
            flag = true;
        }
        return flag;
    }

    public static Set<LevelOfEvidence> getPublicLevels() {
        return new HashSet<>(PUBLIC_LEVELS);
    }

    public static Set<LevelOfEvidence> getSensitiveLevels() {
        return new HashSet<>(CollectionUtils.intersection(PUBLIC_LEVELS, THERAPEUTIC_SENSITIVE_LEVELS));
    }

    public static Set<LevelOfEvidence> getResistanceLevels() {
        return new HashSet<>(CollectionUtils.intersection(PUBLIC_LEVELS, THERAPEUTIC_RESISTANCE_LEVELS));
    }

    public static int getSensitiveLevelIndex(LevelOfEvidence levelOfEvidence) {
        return THERAPEUTIC_SENSITIVE_LEVELS.indexOf(levelOfEvidence);
    }

    public static int getResistanceLevelIndex(LevelOfEvidence levelOfEvidence) {
        return THERAPEUTIC_RESISTANCE_LEVELS.indexOf(levelOfEvidence);
    }

    public static LevelOfEvidence getSensitiveLevelByIndex(int index) {
        return THERAPEUTIC_SENSITIVE_LEVELS.get(index);
    }

    public static LevelOfEvidence getResistanceLevelByIndex(int index) {
        return THERAPEUTIC_RESISTANCE_LEVELS.get(index);
    }

    public static List<LevelOfEvidence> getIndexedPublicLevels() {
        return new ArrayList<>(PUBLIC_LEVELS);
    }

    public static List<LevelOfEvidence> getIndexedTherapeuticLevels() {
        return new ArrayList<>(THERAPEUTIC_LEVELS_WITH_PRIORITY);
    }

    public static Set<LevelOfEvidence> getPrognosticLevels() {
        return new HashSet<>(CollectionUtils.intersection(PUBLIC_LEVELS, PROGNOSTIC_LEVELS));
    }

    public static Set<LevelOfEvidence> getDiagnosticLevels() {
        return new HashSet<>(CollectionUtils.intersection(PUBLIC_LEVELS, DIAGNOSTIC_LEVELS));
    }

    public static Set<LevelOfEvidence> getAllowedCurationLevels() {
        Set levels = new HashSet<>(PUBLIC_LEVELS);
        levels.remove(LevelOfEvidence.LEVEL_0);
        levels.remove(LevelOfEvidence.LEVEL_2B);
        levels.remove(LevelOfEvidence.LEVEL_3B);
        return levels;
    }

    public static ListIterator getTherapeuticLevelsWithPriorityLIstIterator() {
        return THERAPEUTIC_LEVELS_WITH_PRIORITY.listIterator(THERAPEUTIC_LEVELS_WITH_PRIORITY.size());
    }

    public static Boolean areSameLevels(LevelOfEvidence l1, LevelOfEvidence l2) {
        if (l1 == null && l2 == null) {
            return true;
        } else if (l1 != null && l2 != null) {
            return l1.equals(l2);
        } else {
            return false;
        }
    }
}
