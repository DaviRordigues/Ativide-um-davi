package agilebit.ordermanagement.services;

import agilebit.ordermanagement.dtos.entity.MarketMarketIOCDTO;
import agilebit.ordermanagement.dtos.entity.OrderConfigurationDTO;
import agilebit.ordermanagement.dtos.entity.OrderDTO;
import agilebit.ordermanagement.dtos.requests.MarketOrderBuyRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static agilebit.ordermanagement.configs.Constants.USDC;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BulkServiceImpTest {
	
	@Mock
	private BulkOrdersRepository bulkOrdersRepository;
	
	@Mock
	private CoinbaseService coinbaseService;
	
	@InjectMocks
	private BulkServiceImp bulkServiceImp;
	
	private static final BigDecimal currentProductMarketValue = new BigDecimal("60719.1");
	
	
	private static final String BTC_USDC = "BTC-USDC"; // Definindo uma constante
	
	@Value("${strategy.profit}")
	private String profit;
	
	@BeforeEach
	void setup() {
		bulkServiceImp = new BulkServiceImp(bulkOrdersRepository, coinbaseService);
	}
	
	@Test
	void testManageOrder_whenBulkOrderExists() {
		BulkOrdersEntity mockBulkOrdersEntity = createBulkOrdersEntity();
		when(bulkOrdersRepository.findByStatus(BulkStatus.OPEN.toString())).thenReturn(Optional.of(mockBulkOrdersEntity));
		
		when(coinbaseService.getCurrentMarketProductValue(BTC_USDC)).thenReturn(currentProductMarketValue);
		
		when(coinbaseService.getAccountAvailableBalanceValue(USDC)).thenReturn("0.00026372");
		
		CreateOrderResponseDTO buyCreateOrderResponseDTO = createBuyOrderResponseDTO();
		when(coinbaseService.marketOrderBuy(any())).thenReturn(buyCreateOrderResponseDTO);
		
		when(coinbaseService.getOrder(buyCreateOrderResponseDTO.getOrderId())).thenReturn(createBuyOrderDTO());
		
		bulkServiceImp.manageOrder();
		
		verify(bulkOrdersRepository, times(1)).findByStatus(BulkStatus.OPEN.toString());
		//verify(bulkServiceImp, times(1)).manageBulkOrders(mockBulkOrdersEntity);
	}
	
	@Test
	void testManageOrder_whenBulkOrderDoesNotExist() {
		when(bulkOrdersRepository.findByStatus(BulkStatus.OPEN.toString())).thenReturn(Optional.empty());
		when(coinbaseService.getAccountAvailableBalanceValue(USDC)).thenReturn("0.00026372");
		
		CreateOrderResponseDTO buyCreateOrderResponseDTO = createBuyOrderResponseDTO();
		when(coinbaseService.marketOrderBuy(any())).thenReturn(buyCreateOrderResponseDTO);
		
		when(coinbaseService.getOrder(buyCreateOrderResponseDTO.getOrderId())).thenReturn(createBuyOrderDTO());
		
		CreateOrderResponseDTO sellCreateOrderResponseDTO = createSellOrderResponseDTO();
		
		when(coinbaseService.limitOrderGtcSell(any())).thenReturn(sellCreateOrderResponseDTO);
		
		when(coinbaseService.getOrder(sellCreateOrderResponseDTO.getOrderId())).thenReturn(createSellOrderDTO());
		
		bulkServiceImp.manageOrder();
		
		verify(bulkOrdersRepository, times(1)).findByStatus(BulkStatus.OPEN.toString());
	}
	
	private CreateOrderResponseDTO createSellOrderResponseDTO() {
		return CreateOrderResponseDTO.builder()
				.success(true)
				.failureReason("UNKNOWN_FAILURE_REASON")
				.orderId("b752322a-45ba-ae76-39345a64206b")
				.successResponse(SuccessResponse.builder()
						.orderId("b752322a-6cb9-45ba-ae76-39345a64206a")
						.productId("BTC-USDC")
						.side("SELL")
						.clientOrderId("clientOrderId")
						.build())
				.orderConfiguration(OrderConfigurationDTO.builder()
						.marketMarketIoc(MarketMarketIOCDTO.builder()
								.quoteSize("9")
								.build())
						.build())
				.build();
	}
	
	private OrderDTO createSellOrderDTO() {
		return OrderDTO.builder()
				.orderId("b752322a-6cb9-45ba-ae76-39345a64206a")
				.productId("BTC-USDC")
				.userId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
				.orderConfiguration(OrderConfigurationDTO.builder()
						.marketMarketIoc(MarketMarketIOCDTO.builder()
								.quoteSize("9")
								.build())
						.build())
				.side("SELL")
				.clientOrderId("clientOrderId")
				.status("FILLED")
				.timeInForce("IMMEDIATE_OR_CANCEL")
				.createdTime("2024-05-29T01:38:06.834976Z")
				.completionPercentage("100")
				.filledSize("0.0001305276665502")
				.averageFilledPrice("68403.6700000115635715")
				.fee("")
				.numberOfFills("3")
				.filledValue("8.9285714285714286")
				.pendingCancel(false)
				.sizeInQuote(true)
				.totalFees("0.0714285714285714")
				.sizeInclusiveOfFees(true)
				.totalValueAfterFees("9")
				.triggerStatus("INVALID_ORDER_TYPE")
				.orderType("MARKET")
				.rejectReason("REJECT_REASON_UNSPECIFIED")
				.settled(true)
				.productType("SPOT")
				.rejectMessage("")
				.cancelMessage("")
				.orderPlacementSource("RETAIL_ADVANCED")
				.outstandingHoldAmount("0")
				.isLiquidation(false)
				.lastFillTime("2024-05-29T01:38:06.960858598Z")
				.editHistory(List.of())  // Assume uma lista vazia para editHistory
				.leverage("")
				.marginType("UNKNOWN_MARGIN_TYPE")
				.retailPortfolioId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
				.build();
	}
	
	private OrderDTO createBuyOrderDTO() {
		return OrderDTO.builder()
				.orderId("b752322a-6cb9-45ba-ae76-39345a64206a")
				.productId("BTC-USDC")
				.userId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
				.orderConfiguration(OrderConfigurationDTO.builder()
						.marketMarketIoc(MarketMarketIOCDTO.builder()
								.quoteSize("9")
								.build())
						.build())
				.side("BUY")
				.clientOrderId("clientOrderId")
				.status("FILLED")
				.timeInForce("IMMEDIATE_OR_CANCEL")
				.createdTime("2024-05-29T01:38:06.834976Z")
				.completionPercentage("100")
				.filledSize("0.0001305276665502")
				.averageFilledPrice("68403.6700000115635715")
				.fee("")
				.numberOfFills("3")
				.filledValue("8.9285714285714286")
				.pendingCancel(false)
				.sizeInQuote(true)
				.totalFees("0.0714285714285714")
				.sizeInclusiveOfFees(true)
				.totalValueAfterFees("9")
				.triggerStatus("INVALID_ORDER_TYPE")
				.orderType("MARKET")
				.rejectReason("REJECT_REASON_UNSPECIFIED")
				.settled(true)
				.productType("SPOT")
				.rejectMessage("")
				.cancelMessage("")
				.orderPlacementSource("RETAIL_ADVANCED")
				.outstandingHoldAmount("0")
				.isLiquidation(false)
				.lastFillTime("2024-05-29T01:38:06.960858598Z")
				.editHistory(List.of())  // Assume uma lista vazia para editHistory
				.leverage("")
				.marginType("UNKNOWN_MARGIN_TYPE")
				.retailPortfolioId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
				.build();
	}
	
	private CreateOrderResponseDTO createBuyOrderResponseDTO() {
		return CreateOrderResponseDTO.builder()
				.success(true)
				.failureReason("UNKNOWN_FAILURE_REASON")
				.orderId("b752322a-6cb9-45ba-39345a64206a")
				.successResponse(SuccessResponse.builder()
						.orderId("b752322a-6cb9-45ba-ae76-39345a64206a")
						.productId("BTC-USDC")
						.side("BUY")
						.clientOrderId("clientOrderId")
						.build())
				.orderConfiguration(OrderConfigurationDTO.builder()
						.marketMarketIoc(MarketMarketIOCDTO.builder()
								.quoteSize("9")
								.build())
						.build())
				.build();
	}
	
	private MarketOrderBuyRequestDTO createMarketOrderBuyRequestDTO() {
		return MarketOrderBuyRequestDTO
				.builder()
				.clientOrderId("NzdKpGBIMJ20240531201540")
				.productId(BTC_USDC)
				.quoteSize("0.00026372")
				.build();
	}
	
	private BulkOrdersEntity createBulkOrdersEntity() {
		return BulkOrdersEntity.builder()
				.id("66593a45b95d422c901c943c")
				.status("OPEN")
				.sellingValueProposal("69073.56")
				.orders(List.of(
						OrderEntity.builder()
								.side("BUY")
								.status("FILLED")
								.orderId("b752322a-6cb9-45ba-ae76-39345a64206a")
								.productId("BTC-USDC")
								.userId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.orderConfiguration(null)
								.clientOrderId("clientOrderId")
								.createdTime("2024-05-29T01:38:06.834976Z")
								.filledSize("0.0001305276665502")
								.averageFilledPrice("68403.6700000115635715")
								.filledValue("8.9285714285714286")
								.totalFees("0.0714285714285714")
								.totalValueAfterFees("9")
								.orderType("MARKET")
								.retailPortfolioId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.build(),
						OrderEntity.builder()
								.side("BUY")
								.status("FILLED")
								.orderId("b7bb0579-f703-4df4-8325-941e1b276572")
								.productId("BTC-USDC")
								.userId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.orderConfiguration(null)
								.clientOrderId("JpCSNYvpCt20240531130619")
								.createdTime("2024-05-31T16:06:24.220166Z")
								.filledSize("0.0001331932770355")
								.averageFilledPrice("67034.7004540754465699")
								.filledValue("8.9285714285714286")
								.totalFees("0.0714285714285714")
								.totalValueAfterFees("9")
								.orderType("MARKET")
								.retailPortfolioId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.build(),
						OrderEntity.builder()
								.side("SELL")
								.status("OPEN")
								.orderId("ecfdbeef-36ed-4790-a5a6-6e7b17e9c965")
								.productId("BTC-USDC")
								.userId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.orderConfiguration(OrderConfigurationEntity.builder()
										.marketIoc(null)
										.limitGtc(LimitGtcEntity.builder()
												.baseSize("0.00026372")
												.limitPrice("69073.56")
												.build())
										.build())
								.clientOrderId("clientOrderId4")
								.createdTime("2024-05-29T23:23:42.126178Z")
								.filledSize("0")
								.averageFilledPrice("0")
								.filledValue("0")
								.totalFees("0")
								.totalValueAfterFees("0")
								.orderType("LIMIT")
								.retailPortfolioId("2caa05b9-899c-5c6b-a9bd-2732bcb93416")
								.build()
				)).build();
	}
}