import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IUserProfileUpdateState {
  isNew: boolean;
}

export class UserProfileUpdate extends React.Component<IUserProfileUpdateProps, IUserProfileUpdateState> {
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { userProfileEntity } = this.props;
      const entity = {
        ...userProfileEntity,
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
    this.props.history.push('/entity/user-profile');
  };

  render() {
    const { userProfileEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="haliApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : userProfileEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="user-profile-id">ID</Label>
                    <AvInput id="user-profile-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="userIdLabel" for="user-profile-userId">
                    User Id
                  </Label>
                  <AvField
                    id="user-profile-userId"
                    type="text"
                    name="userId"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="imageUrlLabel" for="user-profile-imageUrl">
                    Image Url
                  </Label>
                  <AvField id="user-profile-imageUrl" type="text" name="imageUrl" />
                </AvGroup>
                <AvGroup>
                  <Label id="cityLabel" for="user-profile-city">
                    City
                  </Label>
                  <AvField id="user-profile-city" type="text" name="city" />
                </AvGroup>
                <AvGroup>
                  <Label id="addressLabel" for="user-profile-address">
                    Address
                  </Label>
                  <AvField id="user-profile-address" type="text" name="address" />
                </AvGroup>
                <AvGroup>
                  <Label id="districtLabel" for="user-profile-district">
                    District
                  </Label>
                  <AvField id="user-profile-district" type="text" name="district" />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneNumberLabel" for="user-profile-phoneNumber">
                    Phone Number
                  </Label>
                  <AvField id="user-profile-phoneNumber" type="text" name="phoneNumber" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/user-profile" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
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
  userProfileEntity: storeState.userProfile.entity,
  loading: storeState.userProfile.loading,
  updating: storeState.userProfile.updating,
  updateSuccess: storeState.userProfile.updateSuccess
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
)(UserProfileUpdate);
