import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './fight.reducer';
import { IFight } from 'app/shared/model/fight.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFightUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFightUpdateState {
  isNew: boolean;
}

export class FightUpdate extends React.Component<IFightUpdateProps, IFightUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { fightEntity } = this.props;
      const entity = {
        ...fightEntity,
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
    this.props.history.push('/entity/fight');
  };

  render() {
    const { fightEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="sportstreamApp.fight.home.createOrEditLabel">Create or edit a Fight</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fightEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="fight-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="redCornerLabel" for="redCorner">
                    Red Corner
                  </Label>
                  <AvField
                    id="fight-redCorner"
                    type="text"
                    name="redCorner"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="blueCornerLabel" for="blueCorner">
                    Blue Corner
                  </Label>
                  <AvField
                    id="fight-blueCorner"
                    type="text"
                    name="blueCorner"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="resultLabel">Result</Label>
                  <AvInput
                    id="fight-result"
                    type="select"
                    className="form-control"
                    name="result"
                    value={(!isNew && fightEntity.result) || 'PENDING'}
                  >
                    <option value="PENDING">PENDING</option>
                    <option value="IN_PROGRESS">IN_PROGRESS</option>
                    <option value="FINISHED">FINISHED</option>
                    <option value="NOT_HAPPENED">NOT_HAPPENED</option>
                    <option value="OTHER">OTHER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="resultTypeLabel">Result Type</Label>
                  <AvInput
                    id="fight-resultType"
                    type="select"
                    className="form-control"
                    name="resultType"
                    value={(!isNew && fightEntity.resultType) || 'RED_WIN'}
                  >
                    <option value="RED_WIN">RED_WIN</option>
                    <option value="BLUE_WIN">BLUE_WIN</option>
                    <option value="NOT_DEFINED">NOT_DEFINED</option>
                    <option value="OTHER">OTHER</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="commentLabel" for="comment">
                    Comment
                  </Label>
                  <AvField id="fight-comment" type="text" name="comment" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/fight" replace color="info">
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
  fightEntity: storeState.fight.entity,
  loading: storeState.fight.loading,
  updating: storeState.fight.updating,
  updateSuccess: storeState.fight.updateSuccess
});

const mapDispatchToProps = {
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
)(FightUpdate);
