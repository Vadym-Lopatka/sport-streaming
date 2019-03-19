import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFight } from 'app/shared/model/fight.model';
import { getEntities as getFights } from 'app/entities/fight/fight.reducer';
import { getEntity, updateEntity, createEntity, reset } from './round.reducer';
import { IRound } from 'app/shared/model/round.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRoundUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRoundUpdateState {
  isNew: boolean;
  fightId: string;
}

export class RoundUpdate extends React.Component<IRoundUpdateProps, IRoundUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      fightId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getFights();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { roundEntity } = this.props;
      const entity = {
        ...roundEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/round');
  };

  render() {
    const { roundEntity, fights, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="sportstreamApp.round.home.createOrEditLabel">Create or edit a Round</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : roundEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="round-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="redTotalScoreLabel" for="redTotalScore">
                    Red Total Score
                  </Label>
                  <AvField id="round-redTotalScore" type="string" className="form-control" name="redTotalScore" />
                </AvGroup>
                <AvGroup>
                  <Label id="blueTotalScoreLabel" for="blueTotalScore">
                    Blue Total Score
                  </Label>
                  <AvField id="round-blueTotalScore" type="string" className="form-control" name="blueTotalScore" />
                </AvGroup>
                <AvGroup>
                  <Label for="fight.id">Fight</Label>
                  <AvInput id="round-fight" type="select" className="form-control" name="fightId">
                    {fights
                      ? fights.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/round" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />&nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />&nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  fights: storeState.fight.entities,
  roundEntity: storeState.round.entity,
  loading: storeState.round.loading,
  updating: storeState.round.updating,
  updateSuccess: storeState.round.updateSuccess
});

const mapDispatchToProps = {
  getFights,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RoundUpdate);
