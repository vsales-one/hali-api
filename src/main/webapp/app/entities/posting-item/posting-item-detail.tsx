import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './posting-item.reducer';
import { IPostingItem } from 'app/shared/model/posting-item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPostingItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class PostingItemDetail extends React.Component<IPostingItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { postingItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            PostingItem [<b>{postingItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">Title</span>
            </dt>
            <dd>{postingItemEntity.title}</dd>
            <dt>
              <span id="imageUrl">Image Url</span>
            </dt>
            <dd>{postingItemEntity.imageUrl}</dd>
            <dt>
              <span id="last_modified_date">Last Modified Date</span>
            </dt>
            <dd>
              <TextFormat value={postingItemEntity.last_modified_date} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="last_modified_by">Last Modified By</span>
            </dt>
            <dd>{postingItemEntity.last_modified_by}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{postingItemEntity.description}</dd>
            <dt>
              <span id="pickUpTime">Pick Up Time</span>
            </dt>
            <dd>{postingItemEntity.pickUpTime}</dd>
            <dt>
              <span id="startDate">Start Date</span>
            </dt>
            <dd>
              <TextFormat value={postingItemEntity.startDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="endDate">End Date</span>
            </dt>
            <dd>
              <TextFormat value={postingItemEntity.endDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="pickupAddress">Pickup Address</span>
            </dt>
            <dd>{postingItemEntity.pickupAddress}</dd>
            <dt>
              <span id="latitude">Latitude</span>
            </dt>
            <dd>{postingItemEntity.latitude}</dd>
            <dt>
              <span id="longitude">Longitude</span>
            </dt>
            <dd>{postingItemEntity.longitude}</dd>
            <dt>
              <span id="city">City</span>
            </dt>
            <dd>{postingItemEntity.city}</dd>
            <dt>
              <span id="district">District</span>
            </dt>
            <dd>{postingItemEntity.district}</dd>
            <dt>Category</dt>
            <dd>{postingItemEntity.categoryCategoryName ? postingItemEntity.categoryCategoryName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/posting-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/posting-item/${postingItemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ postingItem }: IRootState) => ({
  postingItemEntity: postingItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PostingItemDetail);
