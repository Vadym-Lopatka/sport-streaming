import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fight.reducer';
import { IFight } from 'app/shared/model/fight.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFightDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FightDetail extends React.Component<IFightDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fightEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Fight [<b>{fightEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="redCorner">Red Corner</span>
            </dt>
            <dd>{fightEntity.redCorner}</dd>
            <dt>
              <span id="blueCorner">Blue Corner</span>
            </dt>
            <dd>{fightEntity.blueCorner}</dd>
            <dt>
              <span id="result">Result</span>
            </dt>
            <dd>{fightEntity.result}</dd>
            <dt>
              <span id="resultType">Result Type</span>
            </dt>
            <dd>{fightEntity.resultType}</dd>
            <dt>
              <span id="comment">Comment</span>
            </dt>
            <dd>{fightEntity.comment}</dd>
          </dl>
          <Button tag={Link} to="/entity/fight" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>&nbsp;
          <Button tag={Link} to={`/entity/fight/${fightEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ fight }: IRootState) => ({
  fightEntity: fight.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FightDetail);
