package jesol

import com.coupang.webapp007.niney.command.TerminalCommand
import com.coupang.webapp007.niney.model.Item
import common.PropertiesHelper
import common.controller.IndexingSupportController
import common.controller.SocketSupportController
import common.socket.event.ISocketEvent
import grails.converters.JSON
import kr.nine.client.product.ProductSearchResult
import nine.client.SearchQuery
import nine.client.search.ProductSearchBuilder
import org.apache.solr.client.solrj.SolrServerException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import grails.util.Holders

@ComponentScan("common.socket")
class ProductController extends SocketSupportController<Product> implements IndexingSupportController {

    @Autowired
    public void prepare(ISocketEvent<Product> iSocketEvent) {
        this.iSocketEvent = iSocketEvent;
    }

    ProductController() {
        super(Product)
    }

    @Override
    def index(Integer max) {
        if(Holders.config.getProperty('grails.nine.active') == 'true'
                && params.q) {
            try {
                searchEngineIndex()
            } catch (SolrServerException e1) {
                super.searchIndex(max)
            } catch (Exception e2) {
                log.error(e2.message)
                super.searchIndex(max)
            }
        } else {
            super.searchIndex(max)
        }
    }

    def start() {

//        ProductIndexer indexer = new ProductIndexer("http://localhost:1245/N");
//        List<Product> list = new ArrayList<Product>();
//        for (int i = 0; i < 10; i++) {
//            org.n.client.product.Product product = new org.n.client.product.Product();
////            Product product = new Product();
//            product.setId(i);
//            product.setTitle("순자리" + i);
//            product.setAmount(100);
//            product.setPrice(10000);
//            product.setCompany("시디");
//            product.setDeliveryCharge(3500);
//            list.add(product);
//        }
//        indexer.update(list.toArray());
//        indexer.commit();

        TerminalCommand terminalCommand = new TerminalCommand()

//        for (int i = 0; i < 5000; i++) {
//            Product product = new Product();
//            product.setCategory(Category.get(1))
//            product.setContent(optionInfo())
//            product.setTitle("순자리" + i);
//            product.setAmount(100+i);
//            product.setPrice(10000+i);
//            product.setCompany("시디");
//            product.setDeliveryCharge(3500+i);
//            product.productInfo = ProductInfo.get(1)
//            product.save()
//            iSocketEvent.insertEvent(product);
//        }

        searchAndMake(terminalCommand, "냉장고")
        searchAndMake(terminalCommand, "청소기")
        searchAndMake(terminalCommand, "세탁기")
        searchAndMake(terminalCommand, "의자")
        searchAndMake(terminalCommand, "신발")
        searchAndMake(terminalCommand, "바지")
        searchAndMake(terminalCommand, "시디")
        searchAndMake(terminalCommand, "책상")
        searchAndMake(terminalCommand, "키보드")
        searchAndMake(terminalCommand, "물티슈")
        searchAndMake(terminalCommand, "카시트")
        searchAndMake(terminalCommand, "냄비")
        searchAndMake(terminalCommand, "보일러")
        searchAndMake(terminalCommand, "커피")
        searchAndMake(terminalCommand, "컵")

        searchAndMake(terminalCommand, "초코")
        searchAndMake(terminalCommand, "점퍼")
        searchAndMake(terminalCommand, "여행")
        searchAndMake(terminalCommand, "토마토")
        searchAndMake(terminalCommand, "수박")
        searchAndMake(terminalCommand, "참외")
        searchAndMake(terminalCommand, "프링클스")
        searchAndMake(terminalCommand, "치킨")
        searchAndMake(terminalCommand, "고등어")
        searchAndMake(terminalCommand, "양파")
        searchAndMake(terminalCommand, "간식")
        searchAndMake(terminalCommand, "오징어")
        searchAndMake(terminalCommand, "물")
        searchAndMake(terminalCommand, "비타민")
        searchAndMake(terminalCommand, "블루베리")
        searchAndMake(terminalCommand, "견과류")
        searchAndMake(terminalCommand, "떡볶이")
        searchAndMake(terminalCommand, "참치")
        searchAndMake(terminalCommand, "어묵")
        searchAndMake(terminalCommand, "키위")

        searchAndMake(terminalCommand, "홍초")
        searchAndMake(terminalCommand, "베지밀")
        searchAndMake(terminalCommand, "삼겹살")
        searchAndMake(terminalCommand, "아이스크림")
        searchAndMake(terminalCommand, "닭갈비")
        searchAndMake(terminalCommand, "랍스터")

        searchAndMake(terminalCommand, "쪼리")
        searchAndMake(terminalCommand, "식물")
        searchAndMake(terminalCommand, "이용권")
        searchAndMake(terminalCommand, "레프팅")
        searchAndMake(terminalCommand, "물감")
        searchAndMake(terminalCommand, "종이")
        searchAndMake(terminalCommand, "테이프")
        searchAndMake(terminalCommand, "어벤저스")
        searchAndMake(terminalCommand, "건담")
        searchAndMake(terminalCommand, "샤프")
        searchAndMake(terminalCommand, "동화")
        searchAndMake(terminalCommand, "연필")
        searchAndMake(terminalCommand, "방학")

        searchAndMake(terminalCommand, "보리")




        render 'start'
    }

    private searchAndMake(TerminalCommand terminalCommand, String keyword) {
        terminalCommand.searchW(keyword)
        List<Item> itemList = terminalCommand.getResult()
        make(itemList)
    }

    private make(List<Item> itemList) {
        for(int i = 0 ; i < itemList.size() ; i++) {
            Item item = itemList[i]
            Product product = new Product()
            product.category = Category.get(1)
            product.title = item.title
            try {
                product.amount = item.intAmount
            } catch (Exception e) {
                product.amount = 0
                continue
            }
            product.company = item.company
            try {
                product.price = item.intPrice
            } catch (Exception e) {
                product.price = 102030
                continue
            }
            product.content = content()
            product.productInfo = ProductInfo.get(1)

            product.save()
            iSocketEvent.insertEvent(product);
        }
    }

    def content() {
        return "《은하철도 999[1]》(일본어: 銀河鐵道999[1] ぎんがてつどうスリーナイン[1] 긴가테쓰도 스리나인[1][*])는 마츠모토 레이지(松本零士)가 창작한 만화 또는 이를 원작으로 하는 애니메이션이다.\n" +
                "\n" +
                "만화는 1977년부터 1979년까지 소년화보사의 소년 만화 잡지 《소년 킹》 에 연재되었다.[2] 애니메이션은 후지 TV를 통해 1978년 9월 14일부터 1981년 4월 9일까지 2년 6개월에 걸쳐 요약편을 포함하여 모두 113화가 방영되었다. 1979년과 1981년에는 린 타로가 감독한 극장판이 제작, 상영되었다. 대한민국에서는 1981년 10월 4일에 오전 8시 특선만화로 1화와 2화가 최초방영되고 일주일후인 10월 11일 12화,13화 화석의전사편을 방영하였다. 그후 방송의 반응이 좋아 113화 전편을 수입하여 1982년 1월 12일 부터 1983년 1월 16일까지 문화방송에서 일요일 아침시간대인 오전 8시에 2편씩 60분으로 묶어 정규방영되었다.\n" +
                "\n" +
                "목차  [숨기기] \n" +
                "1 개요\n" +
                "2 줄거리\n" +
                "3 등장인물\n" +
                "4 노래\n" +
                "5 가상장소 및 사물\n" +
                "6 특별판\n" +
                "7 대한민국에서의 방송\n" +
                "7.1 애니메이션\n" +
                "7.2 주제가 관련<ref>\n" +
                "1차 출처:[http://www.ddanzi.com/articles/article_view.asp?installment_id=42&article_id=866 (폭로)세계 최고의 표절 작곡자가 마상원이라고?]-[[딴지일보]],[[2004년]] [[3월 7일]]\n" +
                "2차 출처(딴지일보 기사에서 퍼온 뒤 수정한 것)[[블로그]] [http://kr.blog.yahoo.com/lej0420/565 행복이 넘치는 라윤라영이네],[http://www.sogmi.com/805 소금이의 행복한 하루]</ref>\n" +
                "7.3 라디오 드라마\n" +
                "8 같이 보기\n" +
                "9 각주\n" +
                "10 바깥 고리\n" +
                "개요[편집]\n" +
                "미야자와 겐지(宮沢賢治)의 《은하철도의 밤(銀河鐵道の夜)》에 등장하는 은하철도에서 영감을 얻었으나, 열차의 운행 체계의 세부 사항은 일본의 옛 철도를 본땄다. 원작자 스스로 은하철도의 밤과 증기기관차에 타고 동경에 갔던 젊은 시절의 체험이 기본이 되었다고 설명하고 있다.\n" +
                "\n" +
                "《우주해적 캡틴 하록(宇宙海敵キャプテンハーロック)》과 함께 애니메이션화가 될 예정이었으나 실현되지 않다가 마츠모토 레이지 원작의 《우주전함 야마토(宇宙戦艦ヤマト)》가 갑자기 인기가 높아져 두 작품 모두 애니메이션화되었다. 같은 시기에 《우주전함 야마토, 새로운 여행》도 TV로 방영되었다.\n" +
                "\n" +
                "만화판은 1977년부터 만화 잡지 《소년킹(少年キング)》'에 연재되어 잡지의 대표적인 작품이 되었다. 1981년에 연재가 종료되어, 당시에는 이것으로 완전히 완결되어 속편은 없는 것으로 되어 있었다.[3] 하지만, 1996년부터 소학관(小學館)의 잡지에 속편을 연재하기 시작하여, 해당 잡지를 거쳐 웹상에 불규칙하게 연재하여 999화까지 그리기로 하였다.\n" +
                "\n" +
                "신작은 41화까지 그려진 상태이다. 여기에서는 극장판 2편의 설정이 들어가 있어 만화판으로부터의 속편은 아니다. 하지만 단행본으로는 만화판 연재로부터 계속되는 번호가 붙여져 간행되고 있다. 신작에서는 세계관이 확대되고 설정이 적극 변경되어 다른 작품인 《우주해적 캡틴 하록》, 《퀸 에메랄다스》, 《천년여왕》, 《하록 사가 : 니벨룽겐의 반지 ~라인의 황금~》와 이야기가 복잡하게 이어졌고, 메텔과 에메랄다스의 관계도 이전의 라이벌에서 자매로 바뀌었다.\n" +
                "\n" +
                "줄거리[편집]\n" +
                "배경은 은하계의 각 행성이 은하철도라 불리는 우주공간을 달리는 열차로 연결된 미래 세계(TV 애니메이션에는 서기 2221년으로 설정[한국에서는 서기 2021년])이다. 우주의 부유한 사람들은 '기계의 몸체'에 정신을 옮겨 기계화 인간이 되어 영원한 생명을 누리고 있었으나 가난한 사람들은 기계의 몸을 얻을 수 없는 데다가, 기계화 인간에게 박해받고 있었다. 그러다가 무료로 기계의 몸을 준다는 안드로메다의 별을 목표로 주인공인 호시노 데츠로(星野鐵郎,한국명 철이)가 신비의 여인 메텔(メーテル)과 은하초특급 999호에 탑승하게 된다. 각 편은 기차가 머무르는 역(별)에서 벌어지는 이야기로 진행되고 마무리되며, 별도로 전체적인 이야기가 진행된다.\n" +
                "\n" +
                "때는 서기 2221년. 지구는 항공우주 교통의 발달로 우주열차가 은하계 끝까지 왕래하고 혹성간의 왕복운행이 활발히 이루어지고 있던 때였으며 지구 안에서는 최첨단 미래도시인 메갈로폴리스가 형성되어서 우주의 부유한 사람들이 '기계의 몸체' 로 전환하여 영원한 생명을 누림과 동시에 쾌적하고 안락한 도시생활을 보내고 있었다. 그러나 그와는 반대로 아직 기계화로 전환되지 못한 보통 육신(育身)의 가난한 사람들은 도시 외곽의 허름하고 어두운 빈민촌으로 쫓겨나 기계화 인간들의 온갖 천대와 박해를 받으며 힘들게 살아가고 있었다. 그러던 어느날 메갈로폴리스에서 출발한다는 '은하철도 999'를 타면 기계화로 전환하지 않은 보통 사람들을 대상으로 무료로 기계몸으로 개조해 준다는 안드로메다의 별까지 갈 수 있다는 소문이 돌았다. 이 소문을 들은 가난한 사람들은 이제 자신들도 기계화 인간이 되면 마음껏 메가로폴리스를 자유롭게 누빌 수 있다는 희망으로 은하철도의 출발지인 메가로폴리스를 주시하게 된다.\n" +
                "\n" +
                "메가로폴리스 외곽 빈민가에서 부모님과 같이 살고있는 테츠로(철이)는 어머니와 함께 눈 내리는 하늘에서 은하열차가 메가로폴리스로 향하는 모습을 보게 된다. 그러나 바로 이 때 눈발 속에서 인간사냥을 한다는 기계백작 일당이 나타나 철이 일행을 노리고 있었고 기계백작은 도망가는 철이 어머니를 암살한다. 철이는 어머니가 쓰러지자 눈물을 흘리며 기계몸을 개조해 준다는 별로 함께 가야한다고 하였지만 철이 어머니는 철이에게 유품격인 목걸이를 건네주고 세상을 떠나고 만다. 철이는 어머니를 여의게 되자 울부짖게 되고 어머니 시신을 가져가는 기계인간들을 보면서 복수를 다짐하게 된다. 어머니를 여의고 홀로 남은 철이는 눈발 속에서 정처없이 떠돌아다니다가 신비스러운 금발미녀인 메텔에 의해 구조되어 그녀로부터 동행한다는 조건으로 은하철도 무임승차권을 받게 된다. 철이는 메텔의 집에 있는 음성탐지기를 통해 자신의 어머니를 사살한 기계인간들이 주변에 있다는 사실을 알게 되자 분노의 눈빛으로 기계인간들을 모두 사살하게 되고 신고를 받고 출동한 경찰들에게 쫓기기도 했으나 메텔에 의해 무사히 구조되게 된다. 그리고 은하철도 999를 타기위해 메가로폴리스에 오게 되었지만 이미 철이에 대한 수배령이 발령되어서 경찰들이 추적을 하고 있는 중이었다. 철이와 메텔은 경찰들을 따돌리고 메가로폴리스 기차역으로 가서 은하철도 999에 탑승하게 되고 999호는 기적을 울리며 첫 출발을 하게 된다. 그리고 철이는 999호를 통해 메가로폴리스의 야경을 바라보며 멀고도 기나긴 우주여행을 통해 여러가지 경험을 쌓게 되는데....\n" +
                "\n" +
                "마침내 철이와 메텔은 기나긴 우주여행 끝에 드디어 오랜 갈망을 해 왔던 기계 모성인 프로메슘 행성에 도착하게 되고 마침 그 곳에서 행성 안내양으로 활동한다는 지니와 함께 행성 중심부로 이동하게 되는데 이 때 메텔은 철이를 지니에게 맡겨두고 자신은 아버지인 닥터 반과 프로메슘을 만나러 홀로 나가게 된다. 한편 철이는 지니의 안내를 받으며 행성 중심부를 구경하게 되었는데 온갖 유흥과 향락을 즐겨하는 기계인간들을 보고 묘한 의심을 갖게 되었다가 고층건물에서 투신자살로 뛰어내린 중년 기계인간을 통해서 메텔이 프로메슘의 외동딸이라는 놀라운 사실을 알아내게 된다. 그리고 메텔은 마침내 어머니 프로메슘을 알현하기 위해 성전으로 나서게 되지만 철이가 성(城) 밖에서 자신을 찾는다는 프로메슘 부하의 보고를 듣고 철이를 만나게 된다. 하지만 이미 메텔의 정체를 알게 된 철이는 경계와 의심의 눈빛으로 메텔을 보게 되는데 때마침 지니를 통해서 자신을 비롯한 우주행성의 여러 소년들을 리더로 삼아서 우주세계를 기계화한다는 프로메슘의 의도를 듣게되자 아무리 유한하고 짧은 인생이라 하더라도 육신을 가진 보통 인간으로 살아가겠다며 결국 기계인간이 되는 것을 포기하기로 결정하게 된다.\n" +
                "\n" +
                "이 소식을 듣게 된 프로메슘은 철이의 행동을 자신에 대한 도전과 반역행위로 간주하고 바로 부하를 소환하여 철이를 은하철도 999호에 태워서 블랙홀로 낙하시키라는 사실상 처형이나 다름없는 잔인한 명령을 내린다. 이를 엿듣게 된 메텔은 닥터 반의 요청하에 따라 캡슐을 용광로로 투하하려 했지만 이미 눈치챈 프로메슘에 의해 발각되어 투하를 포기하며 프로메슘에 의해 옥살이를 하게 되고 철이 역시 프로메슘 부하들에 의해 999호로 강제탑승되어 블랙홀로 낙하될 위기에 처하게 된다. 하지만 철이와 차장은 이대로 죽을 수는 없다는 투지를 갖고 999호를 통해 블랙홀 탈출을 시도하지만 결국 프로메슘의 행성중력으로 인해 시스템이 마비되는 사고가 발생하고 말았다. 그리고 옥살이를 하던 메텔은 철이가 위험에 처함을 느끼며 탈옥을 하게 되고 999호를 잡던 중력을 제거하게 된다. 이 때 철이는 메텔의 도움으로 중력이 해제되자 수동으로 999호를 몰아가며 프로메슘 행성으로 되돌아오게 된다. 한편 프로메슘은 999호 탈출소식에 철이와 메텔을 잡으라는 명령을 내리게 되고 철이는 프로메슘 행성으로 잠입하다가 자살을 시도하려는 지니를 만나 그녀의 자살을 만류시키고 메텔을 만나게 되어서 지니의 도움과 희생으로 닥터 반의 캡슐을 가지며 용광로로 투하하려 한다. 하지만 이를 눈치챈 프로메슘이 메텔을 인질로 잡으면서 위기를 맞게 되지만 캡슐이 철이에 의해 떨어지게 되면서 프로메슘은 마지막까지 철이와 메텔을 잡으려 했지만 결국 용광로의 용암에 맞아 사라져 버리게 된다.\n" +
                "\n" +
                "행성이 붕괴 조짐을 보이자 미처 대피하지 못한 기계인간들을 보면서 철이와 메텔은 위기에 처하면 보통 육신을 가진 자신들이 더 강해보인다는 것을 느끼게 되면서 999호에 오르게 된다. 프로메슘 행성의 중력 때문에 시스템이 마비되었던 999호는 기적을 우렁차게 울리며 프로메슘 행성을 빠져나가는데 성공한다. 그 이후 메텔은 철이에게 이별을 알리는 편지를 내주고 자신은 777호에 다른 소년과 함께 승차하며 철이와의 이별을 고하게 되고 철이 역시 메텔과의 이별을 고하며 소년시절을 마감하게 된다.\n" +
                "\n" +
                "《은하철도 999》가 17년의 세월이 지나 또 다시 발진한다. 용감한 소년 데쓰로, 메텔, 999호에 타는 상냥한 차장, 그리고 우주해적 캡틴 하록이나 해적여왕 에메랄다스 등 마쓰모토 레이지의 친숙한 캐릭터가 새로운 구상과 성대한 스케일로 다시 살아난다. 이야기는 지난번 여행의 1년 후로부터 시작된다. 지구는 신정부 아래 표면상의 평화를 유지한다. 지하 10 km의 지하세계는 얼어붙고, 피지배자들은 미래가 없는 비참한 삶을 살고 있다. 이전에 여왕을 쓰러뜨리고 영웅으로서 맞이할 수 있었던 데쓰로도 살아남은 프로메슘의 후손들과 추종자들에 의해 지하감옥에 감금되어 있다. 그런 데쓰로의 귀에 다시 그리운 기적이 울린다. 지하감옥을 탈주한 데쓰로는 메텔을 다시 만나고 999호에 오른다.\n" +
                "\n" +
                "등장인물[편집]\n" +
                "성우진의 괄호는 MBC(1996년)/EBS방영 당시이며,성우는 왼쪽부터 일본/1980년 방영판/1996년 재더빙판 순이다.\n" +
                "호시노 데츠로(철이)\n" +
                "본작의 남주인공이자 지구의 메갈로폴리스 근처 빈민촌에서 살고 있는 소년. 어머니와 함께 은하철도가 메갈로폴리스에 들어오는 모습을 보게 되었다가 인간사냥을 한다는 기계백작으로부터 어머니를 여의고 고아가 되어 방랑하다가 수수께끼의 여인인 메텔에 의해 구조되어 그녀로부터 은하철도 무임승차권을 받아 기계인간으로 개조해준다는 행성으로 가기 위해 은하철도 999호에 승차하며 우주여행을 떠나게 된다. 성격상 모험심이 강하기도 하지만 어머니를 여읜 후로는 마더 콤플랙스 증상이 심해, 엄마를 연상시키는 여자에 대해 아주 민감하다. 마지막에 기계모성인 프로메슘 행성에 왔을 때는 프로메슘의 야욕과 행성인들의 행동을 보고나서 기계인간이 되기를 포기하고 메텔과 이별을 하며 소년시절을 마감한다.\n" +
                "성우: 노자와 마사코/우문희/최수민(백록 비디오)/(이미자)\n" +
                "메텔\n" +
                "본작의 여주인공이자 땅딸막한 철이에 비해 늘씬하고 신비스러운 여성. 긴 금발에 오렌지빛 눈동자를 가졌으며 두꺼워 보이는 블랙코트를 입고 있다. 기계의 몸을 갖고자 하는 철이에게 999호 승차권을 주어 함께 기계화 모성으로 떠나게 되었으며 기계화 모성을 지배하는 프로메슘의 딸이다. 영원히 시간의 흐름 속을 여행하는 여자이다. 그녀가 블랙코트를 입고있는 것은 프로메슘에 의해 희생된 사람들의 명복(冥福)을 빈다는 뜻에서 입은 상복(喪服)이며 작품 마지막에 메텔의 대사를 통해서 공개되었다.\n" +
                "성우: 이케다 마사코/정희선/정경애(백록 비디오)/(송도영)\n" +
                "차장\n" +
                "늘 온몸을 제복으로 가리고 다니는 은하철도 999호의 차장. 시리즈의 내용 중 일부분에서 그의 몸이 투명 인간인 듯한 설정이 나오고, '안드로메다의 아라비안나이트'편에서 회오리 바람처럼 나오기도 하여 많은 이들의 궁금증을 자아내기도 하였다. 소심한 성격의 소유자인 듯하나, 첫 사랑과의 슬픈 재회편을 통해 의외로 넓은 아량과 진실된 마음을 가진 사람이라는 것이 드러나기도 했다.\n" +
                "성우: 기모쓰키 가네타/(김기현)/장광(백록 비디오)\n" +
                "크레아\n" +
                "유리 몸을 지닌 999호의 승무원. 아름다운 몸을 원해 유리로 된 몸을 가졌으나 그러한 자신에 대해 후회하고 있다. 마지막에는 산산히 부셔져 버린다.\n" +
                "성우: 이치류사이 하루미/(기경옥)/문지현(백록 비디오)\n" +
                "안탈리우스\n" +
                "우주 곳곳을 돌아다니며 도적질을 하고 있는 인물. 은하철도 999호가 장기 정차를 하는 틈을 타서 열차 밑에 몰래 탑승하여 승객들을 인질로 삼으며 돈을 요구하게 된다. 하지만 본심상으로는 따스한 마음씨를 지닌 아이들의 아버지이기도 하다.\n" +
                "성우: (안지환)\n" +
                "프로메슘\n" +
                "기계화 모성의 여왕이자 메텔의 어머니. 전 우주세계를 기계제국으로 만들려는 야욕을 갖고 있으며 철이와 메텔을 잡으려다가 닥터 반이 봉인된 '에너지 캡슐' 에 의해 분출된 용광로 용암에 맞아 사라져버린다. 메텔의 대사에 의하면 옛날엔 사랑이 많았다고 말한 것으로 보아 원래는 성격이 온화했던 것으로 추정된다.\n" +
                "성우: 기노야마 료코/박경혜/(정희선)\n" +
                "닥터 반\n" +
                "메텔의 아버지로 지금은 '에너지 캡슐' 팬던트에 혼령만 봉인되어 있다. 메텔에게 기계모성을 파괴하는 방법을 알려준다.\n" +
                "성우: 긴자 반조/(김기현)/탁원제(백록 비디오)\n" +
                "호시노 카나에(철이 엄마)\n" +
                "철이의 어머니로 상냥하고 다정한 성격을 지녔으며 눈밭에서 기계백작에게 사살 표적이 되자 철이와 함께 피하던 중 사살되어 그 자리에서 죽었다. 시신은 기계백작 일당들이 가져가서 박제화시켰다.\n" +
                "성우:쓰보이 아키코/김나연/(정희선)\n" +
                "기계백작\n" +
                "인간사냥꾼의 두목이며 기계인간이 되지못한 보통사람들을 대상으로 사냥을 즐기는 기계인간. 눈밭에서 철이와 철이 엄마를 쫓던 중 철이 엄마를 사살하였다가 나중에 가서 철이에 의해 부하들과 함께 저택에서 사살되었다. 죽은 사람의 시신을 이용해 박제를 만든다는 일설이 있다.\n" +
                "성우 : 시바타 히데카츠/(안지환)/강구한(백록 비디오)\n" +
                "미래 (지니)\n" +
                "기계화 모성에서 행성 가이드를 맡고 있는 유리의 나신으로 등장한 여성. 기계화 모성에 도착한 철이의 안내를 맡았으며 마지막에 프로메슘이 손에 쥐고 있던 닥터 반의 에너지 캡슐을 빼앗은 후 철이에게 넘겨주고 숨을 거둔다.\n" +
                "(한국판 더빙에서는 지니라는 이름으로 나왔다.)\n" +
                "\n" +
                "999호 메인 컴퓨터\n" +
                "999호의 각종 장치를 제어,판단하고 열차를 무인 운전하는 컴퓨터.\n" +
                "성우:도타니 고지(8화,50화~113화)키튼 야마다(14화~45화)/(김정애)외 한 명\n" +
                "해설\n" +
                "이야기의 끝에서 작가가 남기고 싶은 말이 나오는 부분으로, 이야기에 여운을 남긴다.\n" +
                "성우:다카기 히토시/(김용식)/탁원제(백록 비디오)\n" +
                "그 외의 성우:이선호, 정미숙, 김영선, 안지환 등\n" +
                "노래[편집]\n" +
                "주제곡: 《은하철도 999》, 가수: 사사키 이사오\n" +
                "가상장소 및 사물[편집]\n" +
                "은하철도 999호\n" +
                "지구의 메갈로폴리스를 출발하여 기계혹성인 프로메슘 행성까지 운행하는 증기기관차형의 우주열차. 겉은 19세기 ~ 20세기동안 운행했던 증기기관차 모습을 하고 있지만 내부는 인공지능의 컴퓨터로 장착되어 있다. 운행은 기관사없이 컴퓨터의 조종에 의해 이루어지며 출발을 할 때는 항상 기적을 우렁차게 울리며 김을 품기도 한다. 역에 정차할 때에는 보통 기관차처럼 레일을 통해 달리게 되지만 우주로 날아갈 때는 비행기와 같이 레일없이 하늘을 날으는 모습을 보이며 겉면과 측면에 '999' 라는 숫자가 새겨져 있다. 마지막 프로메슘 때는 컴퓨터의 고장으로 철이가 수동으로 운행했던 적이 있다. 철이와 메텔을 태우며 이들의 우주여행을 도와주는 동반자이기도 하다.\n" +
                "\n" +
                "메갈로폴리스\n" +
                "서기 2221년 지구에서 발달하게 된 최첨단 미래도시로 주로 기계화가 된 기계인간들이 주(主) 시민이며 이 곳의 정식시민이 되기 위해서는 부자(富者)이거나 기계화로 개조되었거나 개조할 수 있는 사람들만 시민 인정을 받는다. 그렇지 못한 가난하고 기계화로 개조할 수 없는 보통 육신의 사람들은 도시 외곽의 빈민촌에서 지내게 되며 메갈로폴리스 도시로의 출입은 할 수 없다.\n" +
                "\n" +
                "빈민촌\n" +
                "메갈로폴리스 외곽 구석진 곳에 위치해 있으며 이 곳에는 기계화 되지 못한 가난한 보통 육신의 사람들이 지내는 곳으로 호화로운 메갈로폴리스 도시와는 달리 허름하고 초라한 분위기를 띄고 있다. 철이와 철이 부모님도 이 곳에서 지내고 있다.\n" +
                "\n" +
                "기계화 인간 (기계인간)\n" +
                "겉모습은 보통인간을 닮아있지만 부속품을 개조하거나 교체를 하면 2천년 이상을 장수할 수 있는 능력이 있으며 기계화 인간이 되면 메갈로폴리스 시민권을 받게 되고 기계화 되지 못한 보통 육신의 사람들을 박해하고 억압하기도 하며 직접 사냥 및 포획까지도 한다. 하지만 대피능력은 보통 인간에 비해 뒤떨어지는 편이며 마지막 프로메슘 때 기계인간들이 철이와 메텔이 달리는 모습을 보고 구조요청을 하기도 하였다.\n" +
                "\n" +
                "프로메슘 행성 (기계 모성)\n" +
                "이 작품의 마지막에 나오게 된 행성이자 은하철도 999호의 종착역 역할을 했던 행성. 행성이름은 작품 마지막에 가서야 처음으로 공개되었으며 철이가 오랫동안 갈망해왔던 곳이기도 했지만 사실은 행성의 여왕이자 메텔의 어머니인 프로메슘의 독재와 야욕이 가득했던 곳이기도 하다. 사실 처음에 나왔던 기계화 인간 무료 개조 역시 프로메슘이 우주 정복을 위한 야욕과 계략으로 꾸며진 것이었으며 이를 위해서는 철이와 같은 용감한 소년이 리더역할을 하는 것이 필요했었다. 이 행성에는 프로메슘에 의해 기계화로 개조된 인간들이 주로 유흥 등을 즐기며 방탕한 삶을 보내고 있지만 막판에 행성이 닥터 반에 의해 자폭하게 되면서 대부분 대피를 하지못해 희생되었고 행성도 블랙홀 속으로 사라지게 되었다.\n" +
                "\n" +
                "특별판[편집]\n" +
                "소년의 여정, 그리고 이별\n" +
                "너는 전사와 같이 살아갈 수 있는가\n" +
                "영원한 나그네 에메랄다스\n" +
                "너는 어머니처럼 사랑할 수 있는가\n" +
                "유리의 크레아\n" +
                "대한민국에서의 방송[편집]\n" +
                "애니메이션[편집]\n" +
                "대한민국에서는 1982년 1월 2일부터 1983년 1월 16일까지 일요일 오전 8시에 정규방영되었고, 1996년 11월부터 1997년 4월까지 재더빙하여 방영하였다. 이후 2008년 7월 14일부터 한국교육방송공사(EBS)에서 방영[4] 되어 12월 18일 종영하였다. 하지만 종영이후에도 매주 일요일 오전 11시 15분에서 12시 15분까지 3편씩 방영했다. 2009년 10월 18일부터 매주 일요일 1시 45분으로 옮겨서 1편을 방영하고 있다. 참고로 EBS 방영분은 1996년에 MBC에서 재더빙한 것이다. 더빙된 한국 방영본의 주제가는 가수 김국환과 민경옥이 불렀다.\n" +
                "\n" +
                "방영 당시에는 ‘단순한 공상과학영화가 아니라 어른들도 즐겨 본다’고 보도되었다. 애니메이션 뿐 아니라 라디오 방송, 카세트 테이프로도 판매되었다.[5]\n" +
                "\n" +
                "주제가 관련[6]주제가 관련<ref>\n" +
                "1차 출처:[http://www.ddanzi.com/articles/article view.asp?installment id=42&article id=866 (폭로)세계 최고의 표절 작곡자가 마상원이라고?]-[[딴지일보]],[[2004년]] [[3월 7일]]\n" +
                "2차 출처(딴지일보 기사에서 퍼온 뒤 수정한 것)[[블로그]] [http://kr.blog.yahoo.com/lej0420/565 행복이 넘치는 라윤라영이네],[http://www.sogmi.com/805 소금이의 행복한 하루]</ref>\n" +
                "한국판의 첫 주제가는 '눈물 실은 은하철도'라는 곡으로 《은하철도 999》의 우리말 녹음연출을 담당했던 박순웅 PD가 작사하고 마상원이 작곡했다. 방송 당시에는 그 엄청난 인기로 인하여, 80년대 중반까지도 은하철도 999의 주제가라고 하면 이 노래를 틀어줄만큼 잘 알려진 곡이었다. 그러나 정작 방송사인 MBC에서는 외면을 받았는데, 그 이유는 곡의 분위기가 슬픈 관계로 아이들이 보는 만화영화에는 적합하지 않다는 판단 때문이었다. 그런 까닭에 1화부터 5화까지만 이 주제가로 방영되고, 6화부터는 일본판 원곡의 곡을 번안하여 주제가로 사용하였다.[7] 그러나 이 곡은 MBC 방영 당시 삽입곡으로 사용했고, 백록 비디오 프로덕션판 《은하철도 999》 비디오 발매 당시 이곡이 주제가로 수록되기도 했다.\n" +
                "1980년대에 현대음반에서 발매한 '은하철도 999' 3부작을 보면 작사/작곡에 김관현이라고 적혀있다. 그런데 이 곡은 같은 현대음반의 'TV 만화영화 총집합'이라는 테이프를 보면 김관현 작사/금수레[8] 작곡으로 표시되어 있다. 당시의 만화영화 주제가들은 일본곡들을 번안한 뒤, 그 흔적을 지우기위해 유명인의 명의를 무단으로 도용한 것이다. 그러고는 이 곡이 한국에서 만들어진 곡이라고 속이고 있었던 것이다.\n" +
                "김관현의 주제가가 마상원의 주제가를 제치고 히트한 데에는 또 다른 어두운 면이 있다. 당시 김관현이 제작한 은하철도999 주제가가 여러 부분에서 히트를 치자 김관현은 그 상업성을 알아차리고 현대음반을 통해 '은하철도 999' 드라마 음반 계획을 세우게 된다. 그리고 이 과정에서 마상원의 '눈물실은 은하철도'는 녹음과정에서 빼 버리고 자신의 명의로 표절작을 주제곡으로 넣어서 발매하게 된다. 이 앨범은 22만장이나 판매될 정도로 히트를 쳤고, 김관현은 막대한 이윤과 더불어 자신의 이름으로 주제가를 알리게 된다.\n" +
                "라디오 드라마[편집]\n" +
                "MBC 라디오에서는 MBC TV에서 방영한 《은하철도 999》를 라디오 드라마로 각색하여 1982년 9월부터 매일 10분 간 주 5회 방송하기도 했다.[9]\n" +
                "\n" +
                "같이 보기[편집]\n" +
                "마쓰모토 레이지\n" +
                "은하철도 999 극장판\n" +
                "사요나라 은하철도 999: 안드로메다 종착역\n" +
                "은하철도 999: 이터널 판타지\n" +
                "각주[편집]\n" +
                "↑ 이동: 가 나 다 라 제목의 숫자 '999'는 원작에서는 '쓰리 나인'(Three-Nine), 한국판에서는 '구구구'라고 읽는다.\n" +
                "이동 ↑ http://www.tojapan.co.kr/culture/ani/pds_content.asp?number=194\n" +
                "이동 ↑ 마츠모토 레이지의 인터뷰, 《아니메쥬(アニメージュ)》, 덕문서점, 1981년 8월호\n" +
                "이동 ↑ 추억의 만화영화 ‘은하철도 999’ 다시 본다, 《문화일보》, 2008.7.12.\n" +
                "이동 ↑ 올TV外畵 미니시리즈物 크게 人氣, 《동아일보》, 1982.12.23\n" +
                "이동 ↑\n" +
                "1차 출처:(폭로)세계 최고의 표절 작곡자가 마상원이라고?-딴지일보,2004년 3월 7일\n" +
                "2차 출처(딴지일보 기사에서 퍼온 뒤 수정한 것)블로그 행복이 넘치는 라윤라영이네,소금이의 행복한 하루\n" +
                "이동 ↑ 현재 잘 알려진 주제가는 김관현 작사, 금수레 작곡으로 표기되었는데 이 노래는 실제로는 하시모토 쥰 작사, 히라오 마사아키 작곡의 일본 원곡을 반이상 표절한 곡으로, 때문에 1996년 재방영 당시에는 작사·작곡자 이름이 삭제된 모습을 볼 수 있다. 결국 당시 마상원씨는 무척 화를 냈지만 별다른 방도가 없었으며 현재의 한국판 주제가는 번안곡 주제가로 바뀌게 된다.\n" +
                "이동 ↑ 당시 유명한 성악가였다.\n" +
                "이동 ↑ MBC라디오 6일부터 秋冬季프로 개편 10개프로 신설 교양강화, 《경향신문》, 1982.8.23\n" +
                "바깥 고리[편집]\n" +
                "\t위키미디어 공용에 관련 미디어 분류가 있습니다.\n" +
                "은하철도 999\n" +
                "(일본어) 은하철도 999 - 공식 웹사이트\n" +
                "(영어) 은하철도 999 - 인터넷 영화 데이터베이스"
    }

    @Override
    def searchEngineIndex() throws SolrServerException {
        SearchQuery searchQuery = new SearchQuery(params)
        ProductSearchBuilder productSearchBuilder = new ProductSearchBuilder(searchQuery)
        ProductSearchResult results = productSearchBuilder.build();
        setHeaderTotalCount(results.getHitCount())

        List<Product> searchIdList = results.getSearchResult()
        if(searchIdList.size() == 0) {
            def empty = []
            respond empty
            return;
        }
        def result = listRestrictResources(searchIdList)

        respond result, model: [("${resourceName}Count".toString()): results.getHitCount()]
    }
}

