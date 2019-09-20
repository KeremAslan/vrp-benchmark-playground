package vrpproblems;

import org.apache.commons.lang3.NotImplementedException;

public enum ProblemType {

  SINTEF("SINTEF");

  private String problemName;

  private ProblemType(final String problemName) {
    this.problemName = problemName;
  }

  @Override
  public String toString() {
    return problemName;
  }

  public static ProblemType getProblemTypeByString(String name) {
    try {
      return  ProblemType.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new NotImplementedException("The problem type" + name + " is not implemented");
    }
  }
}
