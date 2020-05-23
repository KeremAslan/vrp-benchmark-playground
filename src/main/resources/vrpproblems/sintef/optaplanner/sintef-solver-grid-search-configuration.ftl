<?xml version="1.0" encoding="UTF-8"?>

<plannerBenchmark>
    <benchmarkDirectory>local/data/vrp</benchmarkDirectory>
    <warmUpSecondsSpentLimit>30</warmUpSecondsSpentLimit>
    <parallelBenchmarkCount>AUTO</parallelBenchmarkCount>
    <inheritedSolverBenchmark>
        <problemBenchmarks>
            <solutionFileIOClass>vrpproblems.sintef.persistence.SintefReaderAdaptor</solutionFileIOClass>
           <inputSolutionFile>input/sintef/homberger_1000_customer_instances/R1_10_2.TXT</inputSolutionFile>
           <inputSolutionFile>input/sintef/homberger_1000_customer_instances/RC1_10_3.TXT</inputSolutionFile>
           <inputSolutionFile>input/sintef/homberger_1000_customer_instances/RC2_10_1.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/homberger_600_customer_instances/C1_6_2.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/homberger_600_customer_instances/R1_6_2.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/homberger_600_customer_instances/RC1_6_3.TXT</inputSolutionFile>
           <inputSolutionFile>input/sintef/homberger_200_customer_instances/C1_2_2.TXT</inputSolutionFile>
           <inputSolutionFile>input/sintef/homberger_200_customer_instances/R1_2_2.TXT</inputSolutionFile>
           <inputSolutionFile>input/sintef/homberger_200_customer_instances/RC1_2_3.TXT</inputSolutionFile>
        </problemBenchmarks>

        <solver>
            <scanAnnotatedClasses>
                <packageInclude>vrpproblems.sintef</packageInclude>
                <packageInclude>common.optaplanner.basedomain</packageInclude>
            </scanAnnotatedClasses>

            <termination>
                <minutesSpentLimit>1</minutesSpentLimit>
            </termination>
            <!-- <constructionHeuristic>FIRST_FIT</constructionHeuristic> -->

             <moveThreadCount>1</moveThreadCount>

            <constructionHeuristic>
                <constructionHeuristicType>FIRST_FIT</constructionHeuristicType>
<!--                <changeMoveSelector>-->
                    <!-- <valueSelector variableName="previousStandstill"></valueSelector> -->
<!--                    <selectedCountLimit>100</selectedCountLimit>-->
<!--                </changeMoveSelector>-->
            </constructionHeuristic>

            
            <localSearch>
                <localSearchType>LATE_ACCEPTANCE</localSearchType>
                <unionMoveSelector>
                    <changeMoveSelector>
                        <entitySelector id="entitySelector1"/>
                        <valueSelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="entitySelector1"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>20</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </valueSelector>
                    </changeMoveSelector>
                    <swapMoveSelector>
                        <entitySelector id="entitySelector2"/>
                        <secondaryEntitySelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="entitySelector2"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>20</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </secondaryEntitySelector>
                    </swapMoveSelector>
                    <tailChainSwapMoveSelector>
                        <entitySelector id="entitySelector3"/>
                        <valueSelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="entitySelector3"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>20</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </valueSelector>
                    </tailChainSwapMoveSelector>
                </unionMoveSelector>
            </localSearch>
        </solver>
    </inheritedSolverBenchmark>

    <solverBenchmark>
        <name>Easy Score Calculator</name>
        <solver>
            <scoreDirectorFactory>
                <easyScoreCalculatorClass>vrpproblems.sintef.solver.score.SintefEasyScoreCalculator</easyScoreCalculatorClass>
            </scoreDirectorFactory>
        </solver>
    </solverBenchmark>

    <solverBenchmark>
        <name>Drools score calculator</name>
        <solver>
            <scoreDirectorFactory>
               <scoreDrl>vrpproblems/sintef/drools/constraints.drl</scoreDrl>
                <scoreDrl>vrpproblems/sintef/drools/objectives.drl</scoreDrl>
            </scoreDirectorFactory>
        </solver>
    </solverBenchmark>



</plannerBenchmark>
