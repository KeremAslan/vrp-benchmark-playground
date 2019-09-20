package common.solver;

import org.optaplanner.core.impl.domain.variable.listener.VariableListener;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class BaseVariableListener<T> implements VariableListener<T> {

  @Override
  public void beforeEntityAdded(ScoreDirector scoreDirector, T t) {

  }

  @Override
  public void afterEntityAdded(ScoreDirector scoreDirector, T t) {

  }

  @Override
  public void beforeVariableChanged(ScoreDirector scoreDirector, T t) {

  }

  @Override
  public void afterVariableChanged(ScoreDirector scoreDirector, T t) {

  }

  @Override
  public void beforeEntityRemoved(ScoreDirector scoreDirector, T t) {

  }

  @Override
  public void afterEntityRemoved(ScoreDirector scoreDirector, T t) {

  }
}
