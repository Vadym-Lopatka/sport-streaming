import './home.css';

import React from 'react';

import { connect } from 'react-redux';
import { Button, Row, Col, Alert } from 'reactstrap';

import { getSession } from 'app/shared/reducers/authentication';
import { log } from 'react-jhipster';

export interface IHomeProp extends StateProps, DispatchProps {}

export class Home extends React.Component<IHomeProp> {
  static handleClick(event) {
    const id = event.target.id;
    log(id);
    // createEntity({ judgeId: 1, fightId: 1, roundId: 1, value: 2 });

    fetch('http://localhost:8080/api/scores', {
      method: 'POST',
      headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        judgeId: 1,
        fightId: 1,
        roundId: 1,
        value: 2.0
      })
    })
      .then(jsonObject => log(jsonObject))
      .catch(error => document.write(error));

    log('end>>');
  }

  constructor(props) {
    super(props);
    Home.handleClick = Home.handleClick.bind(this);
  }
  componentDidMount() {
    this.props.getSession();
  }

  render() {
    const { account } = this.props;
    return (
      <Row>
        <Col md="9">
          <button id="blue_1_score" type="button" className="btn btn-outline-primary btn-lg btn-block" onClick={Home.handleClick}>
            СИНИЙ _1_
          </button>
          <button id="blue_2_score" type="button" className="btn btn-outline-primary btn-lg btn-block" onClick={Home.handleClick}>
            СИНИЙ _2_
          </button>
          <button id="red_1_score" type="button" className="btn btn-outline-danger btn-lg btn-block" onClick={Home.handleClick}>
            КРАСНЫЙ _1_
          </button>
          <button id="red_2_score" type="button" className="btn btn-outline-danger btn-lg btn-block" onClick={Home.handleClick}>
            КРАСНЫЙ _2_
          </button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated
});

const mapDispatchToProps = { getSession };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Home);
