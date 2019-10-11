<?xml version="1.0" encoding="UTF-8"?>

<plannerBenchmark>
    <benchmarkDirectory>local/data/vrp</benchmarkDirectory>
    <warmUpSecondsSpentLimit>30</warmUpSecondsSpentLimit>
    <parallelBenchmarkCount>3</parallelBenchmarkCount>
    <inheritedSolverBenchmark>
        <problemBenchmarks>
            <solutionFileIOClass>vrpproblems.sintef.persistence.SintefReaderAdaptor</solutionFileIOClass>
            <inputSolutionFile>input/sintef/1000_customer_instances/C1_10_2.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/C1_10_3.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/R1_10_2.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/R1_10_3.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/R2_10_5.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/RC1_10_3.TXT</inputSolutionFile>
            <inputSolutionFile>input/sintef/1000_customer_instances/RC2_10_5.TXT</inputSolutionFile>
        </problemBenchmarks>

        <solver>
            <scanAnnotatedClasses>
                <packageInclude>vrpproblems.sintef</packageInclude>
                <packageInclude>common.basedomain</packageInclude>
            </scanAnnotatedClasses>

            <termination>
                <minutesSpentLimit>3</minutesSpentLimit>
            </termination>
            <constructionHeuristic>FIRST_FIT</constructionHeuristic>
        </solver>
    </inheritedSolverBenchmark>

    <#list [10, 100, 1000] as parabolicDistributionMax>
        <#list ["TABU_SEARCH", "LATE_ACCEPTANCE", "HILL_CLIMBING"] as localSearchType>
    <solverBenchmark>
        <name>${localSearchType} with ${parabolicDistributionMax}</name>
        <solver>
            <moveThreadCount>4</moveThreadCount>
            <scoreDirectorFactory>
                <easyScoreCalculatorClass>vrpproblems.sintef.solver.score.SintefEasyScoreCalculator</easyScoreCalculatorClass>
            </scoreDirectorFactory>
            <localSearch>
                <localSearchType>${localSearchType}</localSearchType>
                <unionMoveSelector>
                    <changeMoveSelector>
                        <entitySelector id="changeMoveSelector"/>
                        <valueSelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="changeMoveSelector"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>${parabolicDistributionMax}</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </valueSelector>
                    </changeMoveSelector>
                    <swapMoveSelector>
                        <entitySelector id="swapMoveSelector"/>
                        <secondaryEntitySelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="swapMoveSelector"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>${parabolicDistributionMax}</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </secondaryEntitySelector>
                    </swapMoveSelector>
                    <tailChainSwapMoveSelector>
                        <entitySelector id="tailChainSwapMoveSelector"/>
                        <valueSelector>
                            <nearbySelection>
                                <originEntitySelector mimicSelectorRef="tailChainSwapMoveSelector"/>
                                <nearbyDistanceMeterClass>vrpproblems.sintef.solver.SintefJobNearbyDistanceMeter</nearbyDistanceMeterClass>
                                <parabolicDistributionSizeMaximum>${parabolicDistributionMax}</parabolicDistributionSizeMaximum>
                            </nearbySelection>
                        </valueSelector>
                    </tailChainSwapMoveSelector>
                </unionMoveSelector>
            </localSearch>
        </solver>
    </solverBenchmark>
            </#list>
    </#list>


</plannerBenchmark>
