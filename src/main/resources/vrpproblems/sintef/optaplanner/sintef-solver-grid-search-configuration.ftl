<?xml version="1.0" encoding="UTF-8"?>

<plannerBenchmark>
    <benchmarkDirectory>local/data/vrp</benchmarkDirectory>
    <warmUpSecondsSpentLimit>30</warmUpSecondsSpentLimit>
    <parallelBenchmarkCount>1</parallelBenchmarkCount>
    <inheritedSolverBenchmark>
        <problemBenchmarks>

            <solutionFileIOClass>vrpproblems.sintef.persistence.SintefReaderAdaptor</solutionFileIOClass>
<#--            <inputSolutionFile>input/sintef/1000_customer_instances/C1_10_1.TXT</inputSolutionFile>-->
            <inputSolutionFile>input/sintef/1000_customer_instances/C1_10_2.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/C1_10_3.TXT</inputSolutionFile>
<#--            <inputSolutionFile>local/data/vrp/sintef/1000_customer_instances/R1_10_1.TXT</inputSolutionFile>-->
<#--            <inputSolutionFile>local/data/vrp/sintef/1000_customer_instances/R2_10_9.TXT</inputSolutionFile>-->
<#--            <inputSolutionFile>local/data/vrp/sintef/1000_customer_instances/RC1_10_1.TXT</inputSolutionFile>-->
    <!--        <inputSolutionFile>local/data/vrp/sintef/1000_customer_instances/C1_10_3.TXT</inputSolutionFile>-->

        </problemBenchmarks>

        <solver>
            <scanAnnotatedClasses>
                <packageInclude>vrpproblems.sintef</packageInclude>
                <packageInclude>common.basedomain</packageInclude>
            </scanAnnotatedClasses>

            <termination>
                <minutesSpentLimit>6</minutesSpentLimit>
            </termination>
            <constructionHeuristic>FIRST_FIT</constructionHeuristic>
        </solver>
    </inheritedSolverBenchmark>

    <solverBenchmark>
        <name>Late Acceptance</name>
        <solver>
            <moveThreadCount>AUTO</moveThreadCount>
            <scoreDirectorFactory>
                <easyScoreCalculatorClass>vrpproblems.sintef.solver.score.SintefEasyScoreCalculator</easyScoreCalculatorClass>
            </scoreDirectorFactory>
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
    </solverBenchmark>




</plannerBenchmark>
