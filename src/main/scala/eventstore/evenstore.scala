package scalawave.eventstore

import monix.reactive.Observable

import java.time.Instant

/** Metadata associated with a stored event
  * @param offset Event offset in the event stream
  */
case class EventMetadata(offset: Long)

trait EventStoreError

case class Record[K, V](key: K, value: V)

trait EventStore[F[_]] {

  /**
    * Subscribe to a stream of events from the underlying data store.
    *
    * @param topic  The topic to subscribe to
    * @param key    The key to filter events by
    * @param offset The starting sequence to get events from (exclusive). None to get from the start.
    * @return Stream of events.
    */
  def subscribe[K, V](topic: String, key: Option[K], offset: Option[Long]): F[Observable[Record[K, V]]]

  /**
    * Save the given event.
    *
    * @return Either an Error or the metadata for the event that was saved.
    *         Other non-specific errors should be available through the container F.
    */
  def append[K, V](topic: String, record: Record[K, V]): F[Either[EventStoreError, EventMetadata]]

  /**
    * Get the latest event.
    *
    * @param key The key
    * @return Single event if found.
    */
  def latest[K, V](topic: String, key: Option[K]): F[Option[Record[K, V]]]
}