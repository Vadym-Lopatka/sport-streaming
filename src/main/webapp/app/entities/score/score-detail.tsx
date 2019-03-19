import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './score.reducer';
import { IScore } from 'app/shared/model/score.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IScoreDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ScoreDetail extends React.Component<IScoreDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { scoreEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Score [<b>{scoreEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="fightId">Fight Id</span>
            </dt>
            <dd>{scoreEntity.fightId}</dd>
            <dt>
              <span id="roundId">Round Id</span>
            </dt>
            <dd>{scoreEntity.roundId}</dd>
            <dt>
              <span id="judgeId">Judge Id</span>
            </dt>
            <dd>{scoreEntity.judgeId}</dd>
            <dt>
              <span id="value">Value</span>
            </dt>
            <dd>{scoreEntity.value}</dd>
          </dl>
          <Button tag={Link} to="/entity/score" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/score/${scoreEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ score }: IRootState) => ({
  scoreEntity: score.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ScoreDetail);
