// import React from 'react';
// import { connect } from 'react-redux';
// import { Link, RouteComponentProps } from 'react-router-dom';
// import { Button, Row, Col, Label } from 'reactstrap';
// import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// // tslint:disable-next-line:no-unused-variable
// import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
// import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
// import { IRootState } from 'app/shared/reducers';
//
// import { getEntity, updateEntity, createEntity, reset } from '../../entities/score/score.reducer';
// import { IScore } from 'app/shared/model/score.model';
// // tslint:disable-next-line:no-unused-variable
// import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
// import { mapIdList } from 'app/shared/util/entity-utils';
//
// export interface JudgeScoreUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}
//
// export interface JudgeScoreUpdateState {
//   isNew: boolean;
// }
//
// export class ScoreUpdate extends React.Component<JudgeScoreUpdateProps, JudgeScoreUpdateState> {
//   constructor(props) {
//     super(props);
//     this.state = {
//       isNew: !this.props.match.params || !this.props.match.params.id
//     };
//   }
//
//   componentWillUpdate(nextProps, nextState) {
//     if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
//       this.handleClose();
//     }
//   }
//
//   componentDidMount() {
//     if (!this.state.isNew) {
//       this.props.getEntity(this.props.match.params.id);
//     }
//   }
//
//   saveEntity = (event, errors, values) => {
//     if (errors.length === 0) {
//       const { scoreEntity } = this.props;
//       const entity = {
//         ...scoreEntity,
//         ...values
//       };
//
//       if (this.state.isNew) {
//
//       } else {
//         this.props.updateEntity(entity);
//       }
//     }
//   };
//
//   handleClose = () => {
//     this.props.history.push('/entity/score');
//   };
//
//   render() {
//     const { scoreEntity, loading, updating } = this.props;
//     const { isNew } = this.state;
//
//     return (
//       <div>
//         <Row className="justify-content-center">
//           <Col md="8">
//             <h2 id="sportstreamApp.score.home.createOrEditLabel">Create or edit a Score</h2>
//           </Col>
//         </Row>
//         <Row className="justify-content-center">
//           <Col md="8">
//             {loading ? (
//               <p>Loading...</p>
//             ) : (
//               <AvForm model={isNew ? {} : scoreEntity} onSubmit={this.saveEntity}>
//                 {!isNew ? (
//                   <AvGroup>
//                     <Label for="id">ID</Label>
//                     <AvInput id="score-id" type="text" className="form-control" name="id" required readOnly />
//                   </AvGroup>
//                 ) : null}
//                 <AvGroup>
//                   <Label id="fightIdLabel" for="fightId">
//                     Fight Id
//                   </Label>
//                   <AvField
//                     id="score-fightId"
//                     type="string"
//                     className="form-control"
//                     name="fightId"
//                     validate={{
//                       required: { value: true, errorMessage: 'This field is required.' },
//                       number: { value: true, errorMessage: 'This field should be a number.' }
//                     }}
//                   />
//                 </AvGroup>
//                 <AvGroup>
//                   <Label id="roundIdLabel" for="roundId">
//                     Round Id
//                   </Label>
//                   <AvField
//                     id="score-roundId"
//                     type="string"
//                     className="form-control"
//                     name="roundId"
//                     validate={{
//                       required: { value: true, errorMessage: 'This field is required.' },
//                       number: { value: true, errorMessage: 'This field should be a number.' }
//                     }}
//                   />
//                 </AvGroup>
//                 <AvGroup>
//                   <Label id="judgeIdLabel" for="judgeId">
//                     Judge Id
//                   </Label>
//                   <AvField
//                     id="score-judgeId"
//                     type="string"
//                     className="form-control"
//                     name="judgeId"
//                     validate={{
//                       required: { value: true, errorMessage: 'This field is required.' },
//                       number: { value: true, errorMessage: 'This field should be a number.' }
//                     }}
//                   />
//                 </AvGroup>
//                 <AvGroup>
//                   <Label id="valueLabel" for="value">
//                     Value
//                   </Label>
//                   <AvField
//                     id="score-value"
//                     type="string"
//                     className="form-control"
//                     name="value"
//                     validate={{
//                       required: { value: true, errorMessage: 'This field is required.' },
//                       number: { value: true, errorMessage: 'This field should be a number.' }
//                     }}
//                   />
//                 </AvGroup>
//                 <Button tag={Link} id="cancel-save" to="/entity/score" replace color="info">
//                   <FontAwesomeIcon icon="arrow-left" />
//                   &nbsp;
//                   <span className="d-none d-md-inline">Back</span>
//                 </Button>
//                 &nbsp;
//                 <Button color="primary" id="save-entity" type="submit" disabled={updating}>
//                   <FontAwesomeIcon icon="save" />
//                   &nbsp; Save
//                 </Button>
//               </AvForm>
//             )}
//           </Col>
//         </Row>
//       </div>
//     );
//   }
// }
//
// const mapStateToProps = (storeState: IRootState) => ({
//   scoreEntity: storeState.score.entity,
//   loading: storeState.score.loading,
//   updating: storeState.score.updating,
//   updateSuccess: storeState.score.updateSuccess
// });
//
// const mapDispatchToProps = {
//   getEntity,
//   updateEntity,
//   createEntity,
//   reset
// };
//
// type StateProps = ReturnType<typeof mapStateToProps>;
// type DispatchProps = typeof mapDispatchToProps;
//
// export default connect(
//   mapStateToProps,
//   mapDispatchToProps
// )(ScoreUpdate);
