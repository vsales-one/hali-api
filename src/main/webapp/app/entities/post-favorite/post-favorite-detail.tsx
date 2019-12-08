import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './post-favorite.reducer';
import { IPostFavorite } from 'app/shared/model/post-favorite.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostFavoriteDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PostFavoriteDetail extends React.Component<IPostFavoriteDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { postFavoriteEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            PostFavorite [<b>{postFavoriteEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="dateFavorited">Date Favorited</span>
            </dt>
            <dd>
              <TextFormat value={postFavoriteEntity.dateFavorited} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>Posting Item</dt>
            <dd>{postFavoriteEntity.postingItemId ? postFavoriteEntity.postingItemId : ''}</dd>
            <dt>User Profile</dt>
            <dd>{postFavoriteEntity.userProfileId ? postFavoriteEntity.userProfileId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/post-favorite" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/post-favorite/${postFavoriteEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ postFavorite }: IRootState) => ({
  postFavoriteEntity: postFavorite.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostFavoriteDetail);
