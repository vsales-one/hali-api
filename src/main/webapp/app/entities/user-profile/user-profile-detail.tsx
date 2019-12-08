import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IUserProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class UserProfileDetail extends React.Component<IUserProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { userProfileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            UserProfile [<b>{userProfileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="userId">User Id</span>
            </dt>
            <dd>{userProfileEntity.userId}</dd>
            <dt>
              <span id="imageUrl">Image Url</span>
            </dt>
            <dd>{userProfileEntity.imageUrl}</dd>
            <dt>
              <span id="city">City</span>
            </dt>
            <dd>{userProfileEntity.city}</dd>
            <dt>
              <span id="address">Address</span>
            </dt>
            <dd>{userProfileEntity.address}</dd>
            <dt>
              <span id="district">District</span>
            </dt>
            <dd>{userProfileEntity.district}</dd>
            <dt>
              <span id="phoneNumber">Phone Number</span>
            </dt>
            <dd>{userProfileEntity.phoneNumber}</dd>
            <dt>
              <span id="latitude">Latitude</span>
            </dt>
            <dd>{userProfileEntity.latitude}</dd>
            <dt>
              <span id="longitude">Longitude</span>
            </dt>
            <dd>{userProfileEntity.longitude}</dd>
            <dt>
              <span id="displayName">Display Name</span>
            </dt>
            <dd>{userProfileEntity.displayName}</dd>
          </dl>
          <Button tag={Link} to="/entity/user-profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/user-profile/${userProfileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ userProfile }: IRootState) => ({
  userProfileEntity: userProfile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserProfileDetail);
