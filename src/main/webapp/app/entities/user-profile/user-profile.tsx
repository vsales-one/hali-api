import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IUserProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IUserProfileState = IPaginationBaseState;

export class UserProfile extends React.Component<IUserProfileProps, IUserProfileState> {
  state: IUserProfileState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { userProfileList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="user-profile-heading">
          User Profiles
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new User Profile
          </Link>
        </h2>
        <div className="table-responsive">
          {userProfileList && userProfileList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={this.sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('userId')}>
                    User Id <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('imageUrl')}>
                    Image Url <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('city')}>
                    City <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('address')}>
                    Address <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('district')}>
                    District <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('phoneNumber')}>
                    Phone Number <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('latitude')}>
                    Latitude <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('longitude')}>
                    Longitude <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={this.sort('displayName')}>
                    Display Name <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {userProfileList.map((userProfile, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${userProfile.id}`} color="link" size="sm">
                        {userProfile.id}
                      </Button>
                    </td>
                    <td>{userProfile.userId}</td>
                    <td>{userProfile.imageUrl}</td>
                    <td>{userProfile.city}</td>
                    <td>{userProfile.address}</td>
                    <td>{userProfile.district}</td>
                    <td>{userProfile.phoneNumber}</td>
                    <td>{userProfile.latitude}</td>
                    <td>{userProfile.longitude}</td>
                    <td>{userProfile.displayName}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${userProfile.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${userProfile.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${userProfile.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No User Profiles found</div>
          )}
        </div>
        <div className={userProfileList && userProfileList.length > 0 ? '' : 'd-none'}>
          <Row className="justify-content-center">
            <JhiItemCount page={this.state.activePage} total={totalItems} itemsPerPage={this.state.itemsPerPage} />
          </Row>
          <Row className="justify-content-center">
            <JhiPagination
              activePage={this.state.activePage}
              onSelect={this.handlePagination}
              maxButtons={5}
              itemsPerPage={this.state.itemsPerPage}
              totalItems={this.props.totalItems}
            />
          </Row>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ userProfile }: IRootState) => ({
  userProfileList: userProfile.entities,
  totalItems: userProfile.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(UserProfile);
